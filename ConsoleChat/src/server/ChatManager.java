package server;

import commands.Command;
import commands.CommandFactory;
import data.Data;
import java.io.IOException;
import java.util.UUID;
import javax.net.ssl.SSLSocket;
import model.Message;
import util.UUIDUtil;

// Is the client is talking to a peer or to the room.

// This will contains a Map of peer-to-peer connection
// Another will be a Map of Room-ClientList where each message of 
// a client will be broadcasted to all the associated room members
public class ChatManager{

    protected static void peerCommand(String userCommand,String sender){
        Command command = CommandFactory.getCommand(userCommand,sender);
        command.execute();
    }
    protected static void roomCommand(String userCommand,String sender){
        Command  command = CommandFactory.getCommand(userCommand,sender);
        command.execute();
    }
    protected static void peerMessage(Message message,String sender,String receiver) throws IOException{
        
        UUID senderUUID = UUIDUtil.getUUID(sender);
        UUID receiverUUID = UUIDUtil.getUUID(receiver);
        if(!Data.isPeerConnection(Data.getPeerConnectionKey(senderUUID, receiverUUID))) return;

        SSLSocket socket = Data.getActiveClientConnection(receiverUUID);
        if(socket != null){
            MessageExchanger.forwardMessageToReceiver(socket,message, sender, receiver);
        }
    }
    protected static void chatMessage(Message message,String sender) throws IOException{
        // This is not the correct way to implement the roomchat but will make it work later.
        UUID senderUUID = UUIDUtil.getUUID(sender);
        SSLSocket socket = Data.getActiveClientConnection(senderUUID);
        if(socket != null){
            MessageExchanger.forwardMessageToReceiver(socket,message, sender,"");
        }
    }
    protected static void activeClient(String client,SSLSocket socket){
        UUID clientUUID = UUIDUtil.getUUID(client);
        Data.addNewActiveClient(clientUUID,socket);
    }
}