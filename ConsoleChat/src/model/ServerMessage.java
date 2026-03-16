package model;

public class ServerMessage {
    private final String from;
    private final String to;

    // Single input constructor for ChatRoom Broadcast
    public ServerMessage(String from) {
        this.from = from;
        this.to = null;
    }

    // Double input cosntructor for Peer Chat
    public ServerMessage(String from, String to){
        this.from = from;
        this.to = to;
    }

    
}
