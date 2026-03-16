package commands;

import server.ClientHandler;

public class CommandFactory {
    public static Command getCommand(String command, ClientHandler client) {
        String[] splits = command.trim().split("\\s+");
        if (splits.length == 0) {
            return new InvalidCommand();
        }

        String cmd = splits[0].toLowerCase();

        switch (cmd) {
            case "/connect" -> {
                if (splits.length < 2) return new InvalidCommand();
                return new StartPeerChatCommand(client, splits[1]);
            }
            case "/disconnect" -> {
                if (splits.length < 2) return new EndPeerChatCommand(client, splits[1]);
                return new EndPeerChatCommand(client, null);
            }
            case "/exit" -> {
                return new ExitCommand(splits[1]);
            }
            default -> {
                return new InvalidCommand();
            }
        }
    }
}
