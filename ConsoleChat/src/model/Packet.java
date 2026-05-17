package model;
// Depending on the null value the purpose of the Packet will be decided on the Server and the Client side for further processing.

import java.io.Serializable;

public class Packet implements Serializable {
    private UserCommand command = null;
    private Message message = null;

    private static final long serialVersionUID = 1L; 

    private String from = "";
    private String to = "";

    public Packet(){}

    public void setCommand(UserCommand command){
        this.command = command;
    }

    public UserCommand getCommand(){
        return this.command;
    }

    public void setMessage(Message message){
        this.message = message;
    }

    public Message getMessage(){
        return this.message;
    }

    public boolean isCommand(Packet packet){
        return this.command != null;  
    }

    public void setFrom(String from){
        this.from = from;
    }

    public void setTo(String to){
        this.to = to;
    }

    public String isFrom(){
        return this.from;
    }
    public String isTo(){
        return this.to;
    }
}
