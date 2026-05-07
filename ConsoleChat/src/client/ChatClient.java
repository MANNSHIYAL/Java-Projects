package client;

// This is the entry point for any clinet from where they can join and chat.

import java.util.Scanner;
import model.ChatType;
import model.Message;
import model.User;
import model.UserCommand;


public class ChatClient {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.println("Please enter you name and hit enter: ");
            String clientName = sc.next();
            
            User client = new User(clientName);
            String peer = "";
            ChatType chatType = null; 
            System.out.println("-----------------INSTRUCTIONS-----------------");
            System.out.println("To connect with peer: /connect PEERNAME");
            System.out.println("To disconnect with peer: /disconnect PEERNAME");
            System.out.println("To exit chat: /exit");
            System.out.println("To send your message hit enter.");
            while (true) {
                String userInput = sc.nextLine();
                UserCommand command = null;
                Message message = null;
                // Condition will check whether it is a command or a simple message and set the variables accordingly.
                if(userInput.toLowerCase().startsWith("/connect")){
                    // Connect Peer
                    // CommandFactory.getCommand(userInput, client);
                    command = new UserCommand("/connect", userInput);
                    if(chatType == null){
                        chatType = ChatType.PEER;
                    }else {
                        // Need to check this think cause I forgot why I have this message.
                        System.out.println("Already connected to a room chat. Please either create a new connection with the peer or leave this room chat first to connect with the peer.");
                    }
                }else if(userInput.toLowerCase(null).startsWith("/disconnect")){
                    // Disconnect Peer
                    // CommandFactory.getCommand(userInput, client);
                    command = new UserCommand("/disconnect", userInput);
                    if(chatType == null){
                        chatType = ChatType.PEER;
                    }else {
                        System.out.println("Already connected to a room chat. Please either create a new connection with the peer or leave this room chat first to connect with the peer.");
                    }
                }else if (userInput.toLowerCase().startsWith("/exit")) {
                    // Disconnect user and clear user connections and clear it from the server
                    command = new UserCommand("/exit", userInput);
                    if(chatType == null){
                        chatType = ChatType.PEER;
                    }else {
                        System.out.println("Already connected to a room chat. Please either create a new connection with the peer or leave this room chat first to connect with the peer.");
                    }
                    break;
                }else {
                    // Message
                    message = new Message(userInput, client.getUser(), peer, ChatType.PEER);
                }
                // After setting the variables here the command/message will be sent to the server as a encoded json.
            }
            System.out.println("-----------------GOOD BYE-----------------");
        }
    }
}
