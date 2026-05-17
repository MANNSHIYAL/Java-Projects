package websocket;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
import model.Packet;
import util.Base64Converter;

public class WebSocketFrame {
        public static void sendMaskedFrame(OutputStream out, Packet packet) throws IOException {

        String text = Base64Converter.encodePacket(packet);

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
