package client;

// This is the entry point for any clinet from where they can join and chat.

import java.util.Scanner;
import model.User;


public class ChatClient {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            String clientName = sc.next();
            
            User client = new User(clientName);
            
            System.out.println("-----------------INSTRUCTIONS-----------------");
            System.out.println("To connect with peer: /connect PEERNAME");
            System.out.println("To disconnect with peer: /disconnect PEERNAME");
            System.out.println("To exit chat: /exit");
            while (true) {
                String userInput = sc.nextLine();
                if(userInput.toLowerCase().startsWith("/connect")){
                    // Connect Peer
                    // CommandFactory.getCommand(userInput, client);
                }else if(userInput.toLowerCase(null).startsWith("/disconnect")){
                    // Disconnect Peer
                    // CommandFactory.getCommand(userInput, client);
                }else if (userInput.toLowerCase().startsWith("/exit")) {
                    // Disconnect user and clear user connections and clear it from the server
                    break;
                }else {
                    // Message
                }
            }
            System.out.println("-----------------GOOD BYE-----------------");
        }
    }
}
