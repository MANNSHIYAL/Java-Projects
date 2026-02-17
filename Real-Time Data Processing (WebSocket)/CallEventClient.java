
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class CallEventClient {
    public static void main(String[] args) throws Exception {

        try {
            // Fetching data from appication.properties
            Properties properties = new Properties();
            properties.load(new FileInputStream("application.properties"));
            int port = Integer.parseInt(properties.getProperty("port"));

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{
                new X509TrustManager(){
                    @Override
                    public X509Certificate[] getAcceptedIssuers() { return null; }
                    @Override
                    public void checkClientTrusted(X509Certificate[] c, String a){}
                    @Override
                    public void checkServerTrusted(X509Certificate[] c, String a){}
                }
            }, new SecureRandom());
    
            SSLSocketFactory socketFactory = sslContext.getSocketFactory();
            SSLSocket socket = (SSLSocket)socketFactory.createSocket("localhost",port);
            socket.startHandshake();

            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();

            // 1. WebSocket Handshake Request
            String key = Base64.getEncoder().encodeToString(new byte[16]);
            String handshake = "GET / HTTP/1.1\r\n"
                            + "Host: localhost\r\n"
                            + "Upgrade: websocket\r\n"
                            + "Connection: Upgrade\r\n"
                            + "Sec-WebSocket-Key: " + key + "\r\n"
                            + "Sec-WebSocket-Version: 13\r\n\r\n";
            out.write(handshake.getBytes());
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while (!(line = reader.readLine()).isEmpty());

            System.out.println("Connected to WSS Server!");
            System.out.println("Enter events in format: ID|STATUS (e.g., 101|ACTIVE)");

            // Thread to listen for Broadcasts from Server
            new Thread(() -> {
               try {
                while (true) { 
                    int b1 = in.read();
                    if(b1 == -1) break;
                    int b2 = in.read();
                    int len = b2 & 0x7F;
                    if (len == 126) len = (in.read() << 8) | in.read();
                    
                    byte[] payload = new byte[len];
                    in.read(payload);
                    System.out.println("\n[BROADCAST]: " + new String(payload, "UTF-8"));
                    System.out.print("> ");
                }
               } catch (Exception e) {
                System.out.println("Error reding data.");
               }
            }).start();
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.print("> ");
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("exit")) break;
                sendMaskedFrame(out, input);
            }
            socket.close();
        } finally {
            System.out.println("Connection ended.");
        }
    }
    private static void sendMaskedFrame(OutputStream out, String text) throws IOException {
        byte[] payload = text.getBytes("UTF-8");
        out.write(0x81);

        if(payload.length <= 125){
            out.write(payload.length | 0x80);
        } else if(payload.length <= 65535) {
            out.write(126 | 0x80);
            out.write((payload.length >> 8) & 0xFF);
            out.write(payload.length & 0xFF);
        }else {
            out.write(127 | 0x80);
            for(int i=7;i>=0;i--){
                out.write((payload.length >> (8*i)) & 0xFF);
            }
        }

        // Random bytes for masking 
        byte[] mask = new byte[4];
        new Random().nextBytes(mask);
        out.write(mask);

        byte[] maskedPayload = new byte[payload.length];

        for (int i = 0; i < payload.length; i++) {
            maskedPayload[i] = (byte) (payload[i]^mask[i%4]);
        }

        out.write(maskedPayload);
        out.flush();
    }
}
