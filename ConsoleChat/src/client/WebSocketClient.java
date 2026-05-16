package client;

import java.io.ByteArrayInputStream;
//  This will handle all the activities on the clinet side which are being handled by the WebSocketServer on the server side.
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Base64;

import javax.net.ssl.SSLSocket;

import model.Packet;

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

                // 2. Guaranteed full read of the Base64 payload
                byte[] payload = new byte[(int) length];
                int totalRead = 0;
                while (totalRead < length) {
                    int read = in.read(payload, totalRead, (int) length - totalRead);
                    if (read == -1) break;
                    totalRead += read;
                }

                // 3. Convert bytes to Base64 String
                String base64String = new String(payload, "UTF-8");

                // 4. Transform Base64 String back to Packet Object
                try {
                    Packet packet = decodePacket(base64String);
                    // handlePacket(packet); // Your logic to route/process the packet
                } catch (Exception e) {
                    System.err.println("Failed to reconstruct Packet: " + e.getMessage());
                }
            }

        }catch(Exception e){

        }
    }

    private Packet decodePacket(String base64Str) throws Exception {
        byte[] data = Base64.getDecoder().decode(base64Str);
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
            return (Packet) ois.readObject();
        }
    }
}
