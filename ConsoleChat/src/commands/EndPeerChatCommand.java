package commands;

import data.Data;

public class EndPeerChatCommand implements Command {
    private final String key;

    public EndPeerChatCommand(String client,String peer){
        key = Data.getPeerConnectionKey(client, peer);
    }

    @Override
    public void execute() {
        disconnectWithPeer();
    }

    private void disconnectWithPeer(){
        Data.removePeerConnection(this.key);
    }
}
