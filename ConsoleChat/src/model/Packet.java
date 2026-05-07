package model;

import commands.Command;

// This Packet will actually be transfered between the Clinet and the server on the network.
// Depending on the null value the purpose of the Packet will be decided on the Server and the Client side for further processing.

public class Packet {
    private Command command = null;
    private Message message = null;

    public void setCommand(Command command){
        this.command = command;
    }

    public Command getCommand(){
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
}
