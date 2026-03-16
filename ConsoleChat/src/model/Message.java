package model;

public class Message {
    private final String message;
    private final ChatType type;
    private final String from;
    private final String to;
    public Message(String message,String from, String to,ChatType type){
        this.message = message;
        this.type = type;
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString(){
        return "{ " + 
        "\"message\": " + this.message +
        "\"type\": " + this.type + 
        "\"from\": " + this.from +
        (this.to != null ? "\"to\": " + this.to : "") + 
        " }";
    }
}
