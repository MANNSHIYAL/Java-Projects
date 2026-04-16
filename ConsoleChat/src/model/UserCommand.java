package model;

import java.util.Map;
import util.JsonUtil;

public class UserCommand {
    private String command;
    private String instruction;

    public UserCommand(String command,String instruction){
        this.command = command;
        this.instruction = instruction;
    }

    public Map<String,Object> getUserCommand(String userCommand){
        return JsonUtil.parseJson(userCommand);
    }
    @Override
    public String toString(){
        return JsonUtil.jsonToString("");
    }
}
