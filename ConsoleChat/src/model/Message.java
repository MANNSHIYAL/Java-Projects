package model;

public class Message {
    private final String message;
    private final ChatType type;

    public Message(String message,ChatType type){
        this.message = message;
        this.type = type;
    }

    public String getMessage(){
        return this.message;
    }

    public ChatType getChatType(){
        return this.type;
    }
}
