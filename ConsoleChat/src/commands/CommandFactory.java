package commands;

import exception.InvalidCommandException;
import server.ClientHandler;

public class CommandFactory {
    public static Command getCommand(String command, ClientHandler client) throws Exception{

        String[] splits = command.split(" ");

        if (splits[0].contains("/connect")) {
            return new StartPeerChatCommand(client,splits[1]);
        }
        if (splits[0].contains("/disconnect")) {
            
        }

        return (Command) new InvalidCommandException();
    }
}
