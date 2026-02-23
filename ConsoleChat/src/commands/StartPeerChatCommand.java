package commands;

import server.ClientHandler;

public class StartPeerChatCommand implements Command {

    private ClientHandler client;
    private String peer;

    public StartPeerChatCommand(ClientHandler client,String peer){
        this.client = client;
        this.peer = peer;
    }

    @Override
    public void execute() {
        connectWithPeer();
    }

    private static synchronized  void connectWithPeer(){
        
    }

}
