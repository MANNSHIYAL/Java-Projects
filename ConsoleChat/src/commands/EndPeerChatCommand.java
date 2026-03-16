package commands;

import data.Data;
import server.ClientHandler;

public class EndPeerChatCommand implements Command {
    private final String key;

    public EndPeerChatCommand(ClientHandler client,String peer){
        key = Data.getPeerConnectionKey("", "");
    }

    @Override
    public void execute() {
        disconnectWithPeer();
    }

    private synchronized void disconnectWithPeer(){
        Data.removePeerConnection(key);
    }
}
