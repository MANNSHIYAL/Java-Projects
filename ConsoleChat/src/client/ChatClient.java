package client;

// This is the entry point for any clinet from where they can join and chat.

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import javax.net.ssl.SSLSocket;
import model.ChatType;
import model.Message;
import model.Packet;
import model.User;
import model.UserCommand;
import websocket.WebSocketFrame;


public class ChatClient {
    public static void main(String[] args) {
        // --- STEP 1: INITIALISE VARIABLES AT THE TOP LEVEL ---
        SSLSocket socket = null;
        WebSocketClientRequestHandShake webSocketClientRequestHandShake = new WebSocketClientRequestHandShake();

        // --- STEP 2: TRY CONNECTING. IF THIS FAILS, EXIT COMPLETELY ---
        try {
            socket = webSocketClientRequestHandShake.requestHandShake();
        } catch (IOException | KeyManagementException | NoSuchAlgorithmException e) {
            System.err.println("\n❌ CONNECTION ERROR: Connection refused.");
            System.err.println("👉 Reason: " + e.getMessage());
        }

        // --- STEP 3: THE ABSOLUTE ENFORCEMENT GUARD ---
        if (socket == null || socket.isClosed()) {
            System.err.println("\n❌ CRITICAL: Could not connect to the server.");
            System.err.println("🛑 Closing client application. Run your server application first!");
            return; // Hard stops execution. Nothing below this line will run.
        }

        
        try (Scanner sc = new Scanner(System.in)) {

            // Listner thread -> Listens to the messages comming from the server.
            WebSocketClient webSocketClient = new WebSocketClient(socket);
            new Thread(webSocketClient).start();

            System.out.println("Please enter you name and hit enter: ");
            String clientName = sc.nextLine();
            String peer = "";
            
            // This is for the client input to send messages/command to the server/peer.
            User client = new User(clientName);
            ChatType chatType = null; 
            System.out.println("-----------------INSTRUCTIONS-----------------");
            System.out.println("To connect with peer: /connect PEERNAME");
            System.out.println("To disconnect with peer: /disconnect PEERNAME");
            System.out.println("To exit chat: /exit");
            System.out.println("To send your message hit enter.");
            System.out.print(">> ");
            while (true) {
                String userInput = sc.nextLine();
                if (userInput == null || userInput.trim().isEmpty()) {
                    System.out.print(">> ");
                    continue;
                }
                UserCommand command;
                Message message;
                Packet packet = new Packet();
                // Condition will check whether it is a command or a simple message and set the variables accordingly.
                if(userInput.toLowerCase().startsWith("/connect")){
                    // Connect Peer
                    // CommandFactory.getCommand(userInput, client);
                    command = new UserCommand("/connect", userInput);
                    if(chatType == null){
                        chatType = ChatType.PEER;
                        packet.setFrom(client.getUser());
                        packet.setTo(userInput.split(" ")[1]);
                        packet.setCommand(command);
                    }else {
                        // Need to check this thing cause I forgot why I have this message.
                        // This message will shown when a user will try to connect to another room or chat while it is conected to a peer.
                        System.out.println("Already connected to a room chat. Please either create a new connection with the peer or leave this room chat first to connect with the peer.");
                    }
                }else if(userInput.toLowerCase().startsWith("/disconnect")){
                    // Disconnect Peer
                    // CommandFactory.getCommand(userInput, client);
                    command = new UserCommand("/disconnect", userInput);
                    if(chatType == null){
                        chatType = ChatType.PEER;
                        packet.setFrom(client.getUser());
                        packet.setTo(userInput.split(" ")[1]);
                        packet.setCommand(command);
                    }else {
                        // This message will shown when a user will try to connect to another room or chat while it is conected to a peer.
                        System.out.println("Already connected to a room chat. Please either create a new connection with the peer or leave this room chat first to connect with the peer.");
                    }
                }else if (userInput.toLowerCase().startsWith("/exit")) {
                    // Disconnect user and clear user connections and clear it from the server
                    command = new UserCommand("/exit", userInput);
                    if(chatType == null){
                        chatType = ChatType.PEER;
                        packet.setFrom(client.getUser());
                        packet.setTo(userInput.split(" ")[1]);
                        packet.setCommand(command);
                        System.out.println("-----------------GOOD BYE-----------------");
                    }else {
                        // This message will shown when a user will try to connect to another room or chat while it is conected to a peer.
                        System.out.println("Already connected to a room chat. Please either create a new connection with the peer or leave this room chat first to connect with the peer.");
                    }
                    return;
                }else {
                    // Message
                    // Make changes to send it through packet
                    message = new Message(userInput, chatType);
                    packet.setFrom(client.getUser());
                    packet.setTo(userInput.split(" ")[1]);
                    packet.setMessage(message);
                }
                // After setting the variables here the command/message will be sent to the server as a encoded json.
                // Encode the packet to send.
                // Packet will be converted to base64 and then will be shared through the
            
                // Send masked frames
                WebSocketFrame.sendMaskedFrame(socket, packet);
            }
        }catch(Exception e){

        }
    }
}   
