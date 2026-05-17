package server;

import commands.Command;
import commands.CommandFactory;
import data.Data;
import java.io.IOException;
import java.io.OutputStream;
import model.Message;

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
    protected static void peerMessage(OutputStream out,Message message,String sender,String receiver) throws IOException{
        MessageExchanger.forwardMessageToReceiver(out,message, sender, receiver);
    }
    protected static void chatMessage(OutputStream out,Message message,String sender) throws IOException{
        MessageExchanger.forwardMessageToReceiver(out,message, sender,"");
    }
    protected static void activeClient(String client){
        Data.addNewActiveClient(client);
    }
}