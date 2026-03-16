package commands;

import java.util.HashMap;

public class CommandList {
    public static HashMap<String,String> commands = new HashMap<>();

    public CommandList() {
        commands.put("connect", "/connect");
        commands.put("disconnect", "/disconnect");
        commands.put("exit", "/exit");
    }
    
}
