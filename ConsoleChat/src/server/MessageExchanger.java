package server;

import java.io.IOException;
import java.io.OutputStream;
import model.Message;
import model.Packet;
import websocket.WebSocketFrame;

public class MessageExchanger {
    public static void forwardMessageToReceiver(OutputStream out,Message message,String sender,String receiver) throws IOException{
        Packet packet = new Packet();
        packet.setMessage(message);
        packet.setFrom(sender);
        if(!receiver.isEmpty()) packet.setTo(receiver);

        WebSocketFrame.sendMaskedFrame(out, packet);
    }
}
