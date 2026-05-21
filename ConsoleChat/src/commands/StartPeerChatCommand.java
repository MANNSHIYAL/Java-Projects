package commands;

import data.Data;
import java.util.UUID;

public class StartPeerChatCommand implements Command {

    private final UUID client;
    private final UUID peer;

    public StartPeerChatCommand(UUID client,UUID peer){
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
