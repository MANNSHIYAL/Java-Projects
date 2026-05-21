package commands;

import java.util.UUID;
import util.UUIDUtil;

public class CommandFactory {
    public static Command getCommand(String command, String client) {
        String[] splits = command.trim().split("\\s+");
        if (splits.length == 0) {
            return new InvalidCommand();
        }

        String cmd = splits[0].toLowerCase();

        switch (cmd) {
            case "/connect" -> {
                UUID clientUUID = UUIDUtil.getUUID(client);
                UUID peerUUID = UUIDUtil.getUUID(splits[1]);
                if (splits.length < 2) return new InvalidCommand();
                return new StartPeerChatCommand(clientUUID, peerUUID);
            }
            case "/disconnect" -> {
                UUID clientUUID = UUIDUtil.getUUID(client);
                UUID peerUUID = UUIDUtil.getUUID(splits[1]);
                if (splits.length < 2) return new EndPeerChatCommand(clientUUID, peerUUID);
                return new EndPeerChatCommand(clientUUID, null);
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
