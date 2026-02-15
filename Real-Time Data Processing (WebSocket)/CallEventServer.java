import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

public class CallEventServer {
    private static Set<ClientHandler> clients = ConcurrentHashMap.newKeySet();
    private static Map<String,CallEvent> activeClients = new ConcurrentHashMap<String,CallEvent>();

    public static void main(String[] args) throws Exception {

        try{
            // Fetching data from appication.properties
            Properties properties = new Properties();
            properties.load(new FileInputStream("application.properties"));
            char[] password = properties.getProperty("ks.password").toCharArray();
            String keyStoreFile = properties.getProperty("keystore");
            int port = Integer.parseInt(properties.getProperty("port"));

            // KeyStore Process
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream(keyStoreFile),password);

            // KeyManagerFactory 
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore,password);

            // SSL Context
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), null, null);

            // SSL Server Socket
            SSLServerSocketFactory serverSocketFactory = sslContext.getServerSocketFactory();
            SSLServerSocket serverSocket = (SSLServerSocket) serverSocketFactory.createServerSocket(port);

            serverSocket.setEnabledProtocols(new String[]{"TLSv1.2", "TLSv1.3"});
            
            System.out.println("WSS Server started on port " + port);

            while (true) { 
                SSLSocket socket = (SSLSocket)serverSocket.accept();
                ClientHandler handler = new ClientHandler(socket);
                clients.add(handler);
                new Thread(handler).start();
            }
        }catch (Exception e){
            System.out.println("Error loading properties.");
        }
    }    
    static void broadcast(CallEvent event){
        activeClients.put(event.callId, event);

        for (ClientHandler client : clients) {
            client.send(event.toString());
        }
    }

    
    static class ClientHandler implements Runnable {

        private InputStream in;
        private OutputStream out;

        ClientHandler(SSLSocket socket) throws IOException {
            this.in = socket.getInputStream();
            this.out = socket.getOutputStream();
        }

        public void send(String message) {
            try {
                byte[] bytes = message.getBytes("UTF-8");
                out.write(0x81);

                if (bytes.length <= 125) {
                    out.write(bytes.length);
                } else if(bytes.length <= 65535) {
                    out.write(126);
                    out.write((bytes.length >> 8) & 0xFF);
                    out.write(bytes.length & 0xFF);
                }else {
                    out.write(127);
                    for(int i=7;i>=0;i--){
                        out.write((bytes.length >> (8*i)) & 0xFF);
                    }
                }
                out.write(bytes);
                out.flush();
            } catch (Exception e) {
                clients.remove(this);
            } 
        }

        @Override
        public void run() {
            try {
                if(!doHandShake()) return;
                while (true) { 
                    byte[] payload = readFrame();   
                    if(payload == null) break;

                    String message = new String(payload,"UTF-8");
                    String[] parts = message.split("\\|");
                    if(parts.length == 2) broadcast(new CallEvent(parts[0], parts[1]));
                }
            } catch (Exception e) {
                clients.remove(this);
            } finally{
                clients.remove(this);
            }
        }

        private byte[] readFrame() throws IOException{
            int b1 = in.read();
            if(b1 == -1) return null;

            int opcode = b1 & 0x0F;
            if(opcode == 8){
                System.out.println("Client requested closure.");
                return null;
            }

            int b2 = in.read();

            boolean masked = (b2 & 0x80) != 0;
            int length = (b2 & 0x7F);

            if (length == 126) {
                length = ((in.read() << 8) | in.read());
            } else if (length == 127) {
                length = 0;
                for (int i = 0; i < 8; i++) {
                    length = ((length << 8) | (in.read() & 0xFF));   
                }
            }


            byte[] payload = new byte[(int) length];
            if(masked){
                byte[] mask = new byte[4];
                in.read(mask);
                for (int i = 0; i < length; i++) {
                    payload[i] = (byte) (in.read()^mask[i%4]);
                }
            }
            return payload;
        }

        private boolean doHandShake() throws Exception {
            Scanner sc = new Scanner(in,"UTF-8");
            String data = sc.useDelimiter("\r\n\r\n").next();
            if(!data.contains("Sec-WebSocket-Key:")) return false;

            String key = data.split("Sec-WebSocket-Key:")[1].split("\r\n")[0].trim();
            String accept = Base64.getEncoder().encodeToString(
                MessageDigest.getInstance("SHA-1")
                .digest((key + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").getBytes("UTF-8"))
            );
            out.write(("HTTP/1.1 101 Switching Protocols\r\n" +
                        "Upgrade: websocket\r\n" +
                        "Connection: Upgrade\r\n" +
                        "Sec-WebSocket-Accept: " + accept + "\r\n" +
                        "\r\n"
                    ).getBytes("UTF-8"));
            return true;
        }
    }
}
