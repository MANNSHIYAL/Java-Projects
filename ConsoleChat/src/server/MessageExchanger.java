package server;

import java.io.IOException;
import javax.net.ssl.SSLSocket;
import model.Message;
import model.Packet;
import websocket.WebSocketFrame;

public class MessageExchanger {
    public static void forwardMessageToReceiver(SSLSocket socket,Message message,String sender,String receiver) throws IOException{
        Packet packet = new Packet();
        packet.setMessage(message);
        packet.setFrom(sender);
        if(!receiver.isEmpty()) packet.setTo(receiver);

        WebSocketFrame.sendMaskedFrame(socket, packet);
    }
}
