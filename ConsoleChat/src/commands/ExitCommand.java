package commands;

import data.Data;

public class ExitCommand implements Command {
    private final String peer;
    public ExitCommand(String peer) {
        this.peer = peer;
    }

    @Override
    public void execute() {
        removeAllPeerConnection();
    }

    private void removeAllPeerConnection(){
        Data.removeAllPeerConnection(this.peer);
    }

}
