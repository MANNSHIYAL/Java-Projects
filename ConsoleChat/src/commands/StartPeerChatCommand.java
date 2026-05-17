package commands;

import data.Data;

public class StartPeerChatCommand implements Command {

    private final String client;
    private final String peer;

    public StartPeerChatCommand(String client,String peer){
        this.client = client;
        this.peer = peer;
    }

    @Override
    public void execute() {
        connectWithPeer();
    }

    private void connectWithPeer(){
        Data.setPeerConnection(this.client, this.peer);
    }

}
