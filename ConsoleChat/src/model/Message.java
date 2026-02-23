package model;

public class Message {
    private String message;
    private String type;
    public Message(String message,ChatType type){
        this.message = message;
        this.type = type.name();
    }

    @Override
    public String toString(){
        return "{" + 
        "\"message\": " + this.message +
        "\"type\": " + this.type +
        "}";
    }
}
