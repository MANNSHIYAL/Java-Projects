package commands;

import data.Data;
import java.util.UUID;

public class EndPeerChatCommand implements Command {
    private final String key;

    public EndPeerChatCommand(UUID client,UUID peer){
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
