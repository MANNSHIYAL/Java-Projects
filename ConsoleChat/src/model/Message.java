package model;

import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L; 
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
