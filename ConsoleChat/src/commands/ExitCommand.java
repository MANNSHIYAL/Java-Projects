package commands;

import data.Data;

public class ExitCommand implements Command {
    private String peer;
    public ExitCommand(String peer) {
        this.peer = peer;
    }

    @Override
    public void execute() {
        removeAllPeerConnection();
    }

    private synchronized void removeAllPeerConnection(){
        Data.removeAllPeerConnection(peer);
    }

}
