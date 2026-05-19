package model;

import java.io.Serializable;
import java.util.Map;
import util.JsonUtil;

// This will be sent by the User to the server and the rest of the commands will be user be the serrver.

public class UserCommand implements Serializable {
    private static final long serialVersionUID = 1L; 
    private final String command;
    private final String instruction;

    public UserCommand(String command,String instruction){
        this.command = command;
        this.instruction = instruction;
    }

    public Map<String,Object> getUserCommand(String userCommand){
        return JsonUtil.parseJson(userCommand);
    }
    @Override
    public String toString(){
        return JsonUtil.jsonToString(this);
    }

    public String getCommand(){
        return this.command;
    }
    public String getInstruction(){
        return this.instruction;
    }
}
