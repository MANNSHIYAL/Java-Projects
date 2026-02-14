
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Scanner;

public class WebSocketServer {
    private static final int port = 8080;
    private static final String webSocketId = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(port);
        System.out.println("Server started as port : " + port);

        while (true) { 
            Socket client = server.accept();
            new Thread(() -> handleClient(client)).start();
        }
    }
    private static void handleClient(Socket client) {
        try (InputStream in = client.getInputStream();
            OutputStream out = client.getOutputStream()) {
            if (!dohandShake(in,out)) {
                return;   
            }
            while (true) { 
                int b1 = in.read();
                if(b1 == -1) return;

                int opcode = b1 & 0x0F;
                if(opcode == 8){
                    System.out.println("Client requested closure.");
                    break;
                }

                int b2 = in.read();
                boolean masked = (b2 & 0x80) != 0;
                long payloadLength = b2 & 0x7F;

                // For variable lengths 
                // 126 for payload >= 126 payload <= 65535
                if (payloadLength == 126) {
                    payloadLength = ((in.read() << 8) | in.read());
                } else if (payloadLength == 127) {
                    payloadLength = 0;
                    for (int i = 0; i < 8; i++) {
                        payloadLength = ((payloadLength << 8) | (in.read() & 0xFF));   
                    }
                }

                if(masked){
                    byte[] mask = new byte[4];
                    in.read(mask);
                    byte[] payload = new byte[(int) payloadLength];

                    for (int i = 0; i < payloadLength; i++) {
                        payload[i] = (byte) (in.read()^mask[i%4]);
                    }

                    String message = new String(payload,"UTF-8");
                    System.out.println("Received (" + payloadLength + " bytes) : " + message);

                    sendTextFrame(out,"Server Echo : " + message);
                }
            }
        } catch (Exception e) {
            System.err.println("Client Error: " + e.getMessage());
        }
    }
    private static boolean dohandShake(InputStream in, OutputStream out) throws Exception {
        Scanner sc = new Scanner(in,"UTF-8");
        String data = sc.useDelimiter("\\r\\n\\r\\n").next();
        String key = "";
        for(String line : data.split("\r\n")){
            if (line.startsWith("Sec-WebSocket-Key:")) {
                key = line.split(":")[1].trim();
                break;
            }
        }
        if (key.isEmpty()) {
            return false;
        }

        String accept = Base64.getEncoder().encodeToString(
            MessageDigest.getInstance("SHA-1")
            .digest((key + webSocketId).getBytes("UTF-8"))
        );

        out.write(("HTTP/1.1 101 Switching Protocols\r\n"
            + "Connection: Upgrade\r\n"
            + "Upgrade: websocket\r\n"
            + "Sec-WebSocket-Accept: " + accept + "\r\n\r\n").getBytes()
        );
        return true;
    }

    private static void sendTextFrame(OutputStream out, String text) throws IOException {
        byte[] bytes = text.getBytes("UTF-8");
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
    }

}
