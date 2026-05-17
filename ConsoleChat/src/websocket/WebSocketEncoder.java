package websocket;

import java.io.IOException;
import model.Packet;
import util.Base64Converter;


// This will encode the message for the transmission 
public class WebSocketEncoder {
    public static String getEncodedMessage(Packet packet) throws IOException{
        return Base64Converter.encodePacket(packet);
    }
}
