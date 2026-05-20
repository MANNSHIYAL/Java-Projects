package client;

import java.io.InputStream;
import javax.net.ssl.SSLSocket;
import model.Packet;
import util.Base64Converter;

public class WebSocketClient implements Runnable {
    private final SSLSocket socket;

    public WebSocketClient(SSLSocket socket) {
        this.socket = socket;
    }
    
    @Override
    public void run() {
        try(InputStream in  = this.socket.getInputStream()){
           while (!socket.isClosed()) {
                int b1 = in.read();
                if (b1 == -1) break; 

                int b2 = in.read();
                if (b2 == -1) break;

                // 1. Handle variable length (Small, Medium, Large)
                long length = b2 & 0x7f;
                if (length == 126) {
                    length = ((in.read() << 8) | (in.read() & 0xff));
                } else if (length == 127) {
                    length = 0;
                    for (int i = 0; i < 8; i++) {
                        length = (length << 8) | (in.read() & 0xff);
                    }
                }

                byte[] mask = new byte[4];
                int maskRead = 0;
                boolean streamClosed = false;
                while (maskRead < 4) {
                    int m = in.read(mask, maskRead, 4 - maskRead);
                    if (m == -1) {
                        streamClosed = true;
                        break;
                    }
                    maskRead += m;
                }
                if (streamClosed) break;


                // 2. Guaranteed full read of the Base64 payload
                byte[] payload = new byte[(int) length];
                int totalRead = 0;
                while (totalRead < length) {
                    int read = in.read(payload, totalRead, (int) length - totalRead);
                    if (read == -1) break;
                    totalRead += read;
                }

                byte[] unmaskedPayload = new byte[payload.length];
                for (int i = 0; i < payload.length; i++) {
                    unmaskedPayload[i] = (byte) (payload[i] ^ mask[i % 4]);
                }

                // 3. Convert bytes to Base64 String
                String base64String = new String(unmaskedPayload, "UTF-8");

                // 4. Transform Base64 String back to Packet Object
                try {
                    // Packet packet = decodePacket(base64String);
                    Packet packet = Base64Converter.decodePacket(base64String);
                    ClientVisibleConsole clientVisibleConsole = new ClientVisibleConsole();
                    clientVisibleConsole.handleReceivedPacket(packet);
                } catch (Exception e) {
                    System.err.println("Failed to reconstruct Packet: " + e.getMessage());
                }
            }

        }catch(Exception e){
            System.err.println("Failed to read the Input: " + e.getMessage());
        }
    }

    // private Packet decodePacket(String base64Str) throws Exception {
    //     byte[] data = Base64.getDecoder().decode(base64Str);
    //     try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
    //         return (Packet) ois.readObject();
    //     }
    // }
}
