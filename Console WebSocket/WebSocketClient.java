
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Base64;
import java.util.Random;
import java.util.Scanner;

public class WebSocketClient {
    public static final String host = "localhost"; 
    public static final int port = 8080; 
    public static void main(String[] args) throws Exception {
        try (Socket socket = new Socket(host,port);
            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream()) {
            String secretKey = Base64.getEncoder().encodeToString(new byte[16]);
            String handShake = "GET /chat HTTP/1.1\r\n" +
                                "Host: " + host + ":" + port + "\r\n" +
                                "Upgrade: websocket\r\n" +
                                "Connection: Upgrade\r\n" +
                                "Sec-WebSocket-Key: " + secretKey + "\r\n" + 
                                "Sec-WebSocket-Version: 13\r\n\r\n";
            out.write(handShake.getBytes());

            // Handshake Response
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            System.out.println("--- Handshake Response ---");
            while (!(line = reader.readLine()).isEmpty()){
                System.out.println(line);
            }

            // Thread to listen server response
            new Thread(() -> {
                try {
                    while (true) { 
                        int b1 = in.read();
                        if(b1 == - 1) return;;
                        int b2 = in.read();
                        int length = b2 & 0x7F;

                        byte[] payload = new byte[length];

                        in.read(payload);
                        System.out.println("\n[Server] : " + new String(payload));
                        System.out.println("> ");
                    }
                } catch (IOException e) {
                    System.out.println("Connection closed.");
                }
            }).start();

            // Send masked messages to server
            Scanner console = new Scanner(System.in);
            System.out.println("Connected! Type a message (or 'exit' to quit):");

            while (true) { 
                System.out.print("> ");
                String message = console.nextLine();
                if ("exit".equalsIgnoreCase(message)) break;

                sendMaskedFrame(out, message);
            }
        } catch (Exception e) {
        }
    }

    private static void sendMaskedFrame(OutputStream out, String text) throws IOException {
        byte[] payload = text.getBytes();
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
