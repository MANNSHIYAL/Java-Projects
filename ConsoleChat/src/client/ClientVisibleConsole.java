package client;

import model.Message;
import model.Packet;

// Here the received packets from the server containig the messages will be processed, client usually sends commands but never receives any commands from the server.

public class ClientVisibleConsole {
    public void handleReceivedPacket(Packet packet){
        if(!packet.isCommand(packet)){
            Message message = packet.getMessage();
            String peer = packet.isTo();

            System.out.println(peer.toUpperCase() + ": " + message.getMessage());
            System.out.print(">> ");
        }
    }
}
