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
        SSLSocket socket = null;
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter you name and hit enter: ");
        String clientName = sc.nextLine();
        WebSocketClientRequestHandShake webSocketClientRequestHandShake = new WebSocketClientRequestHandShake();

        try {
            socket = webSocketClientRequestHandShake.requestHandShake(clientName);
        } catch (IOException | KeyManagementException | NoSuchAlgorithmException e) {
            System.err.println("\n CONNECTION ERROR: Connection refused.");
            System.err.println("👉 Reason: " + e.getMessage());
        }

        if (socket == null || socket.isClosed()) {
            System.err.println("\n CRITICAL: Could not connect to the server.");
            System.err.println("Closing client application. Run your server application first!");
            sc.close();
            return;
        }

        
        try {

            // Listner thread -> Listens to the messages comming from the server.
            WebSocketClient webSocketClient = new WebSocketClient(socket);
            new Thread(webSocketClient).start();
            
            // This is for the client input to send messages/command to the server/peer.
            User client = new User(clientName);
            ChatType chatType = ChatType.PEER; 
            String peer = "";
            System.out.println("-----------------INSTRUCTIONS-----------------");
            System.out.println("Default ChatType is PEER.");
            System.out.println("To connect with peer: /connect PEERNAME");
            System.out.println("To disconnect with peer: /disconnect PEERNAME");
            System.out.println("To exit chat: /exit");
            System.out.println("To send your message hit enter.");
            while (true) {
                System.out.print(">> ");
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
                    chatType = ChatType.PEER;
                    packet.setFrom(client.getUser());
                    peer = userInput.split(" ")[1];
                    packet.setTo(peer);
                    packet.setCommand(command);
                }else if(userInput.toLowerCase().startsWith("/disconnect")){
                    // Disconnect Peer
                    // CommandFactory.getCommand(userInput, client);
                    command = new UserCommand("/disconnect", userInput);

                    packet.setFrom(client.getUser());
                    packet.setTo(userInput.split(" ")[1]);
                    packet.setCommand(command);
                }else if (userInput.toLowerCase().startsWith("/exit")) {
                    // Disconnect user and clear user connections and clear it from the server
                    command = new UserCommand("/exit", userInput);
                    packet.setFrom(client.getUser());
                    packet.setCommand(command);
                    System.out.println("-----------------GOOD BYE-----------------");
                    return;
                }else {
                    // Message
                    // Make changes to send it through packet
                    message = new Message(userInput, chatType);
                    packet.setFrom(client.getUser());
                    packet.setTo(peer);
                    packet.setMessage(message);
                }
                // After setting the variables here the command/message will be sent to the server as a encoded json.
                // Encode the packet to send.
                // Packet will be converted to base64 and then will be shared through the
            
                // Send masked frames
                WebSocketFrame.sendMaskedFrame(socket, packet);
            }
        }catch(IOException e){
            System.err.println(e.getMessage());
        }finally{
            sc.close();
        }
    }
}   
