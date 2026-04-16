package server;

// This class will manage the client like who is talking to whom

import commands.Command;
import commands.CommandFactory;

// Is the client is talking to a peer or to the room.

// This will contains a Map of peer-to-peer connection
// Another will be a Map of Room-ClientList where each message of 
// a client will be broadcasted to all the associated room members
public class ChatManager{

    protected static void peerCommand(String userCommand){
        Command command = CommandFactory.getCommand(userCommand, new ClientHandler());
        command.execute();
    }
    protected static void roomCommand(String userCommand){
        Command  command = CommandFactory.getCommand(userCommand, new ClientHandler());
        command.execute();
    }
    protected static void peerMessage(){

    }
    protected static void chatMessage(){
        
    }
}