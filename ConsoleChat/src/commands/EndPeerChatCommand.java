package commands;

import server.ClientHandler;

public class EndPeerChatCommand implements Command {
    private ClientHandler client;
    private String peer;

    public EndPeerChatCommand(ClientHandler client,String peer){
        this.client = client;
        this.peer = peer;
    }

    @Override
    public void execute() {
        disconnectWithPeer();
    }

    private static synchronized  void disconnectWithPeer(){
        
    }
}
