package websocket;

// This will decode the frame comming form the client and the server.

import java.io.IOException;
import java.io.InputStream;

public class WebSocketDecoder {
    public String decodeMessage(InputStream in) throws IOException{
        String message = "";
        int b1 = in.read();
        if(b1 == -1) return null;

        int opcode = b1 & 0x0F;
        if(opcode == 8){
            System.out.println("Client requested closure.");
            return "disconnect";
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

            message = new String(payload,"UTF-8");
        }
        return message;

    } 
}
