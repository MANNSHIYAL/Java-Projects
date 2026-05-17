package server;

import commands.InvalidCommand;
import java.io.InputStream;
import java.io.OutputStream;
import javax.net.ssl.SSLSocket;
import model.ChatType;
import model.Message;
import model.Packet;
import util.Base64Converter;
import websocket.WebSocketDecoder;

// This class will be responsible to connect all the client activities 
// including the client handshak and the other websocket processes that includes 
// message encode/decode and frame management for the server
public class WebSocketClientHandler {
    public static void handleClient(SSLSocket client) {
        try (InputStream in = client.getInputStream();
            OutputStream out = client.getOutputStream()) {
            WebSocketHandShake handShake = new WebSocketHandShake();
            if (handShake.doHandShake(in, out)) {
                return;   
            }
            // TODO: Add a list of active clients so that whenever a user tries the connect to a peer then if it is not connected server can send a message that the "User you are trying to reach is not active."
            WebSocketDecoder decoder = new WebSocketDecoder();
            // Websocket Server to ChatServer
            while (true) { 
                // Here the "in" will be a base64 string which will be converted to packet object and then that packet will be used by the server to process the packet data
                Packet packetReceived = Base64Converter.decodePacket((decoder.decodeMessage(in)));
                Message messageReceived = packetReceived.getMessage();
                ChatType chatType = messageReceived.getChatType();
                String command = "";
                String userCommand = "";
                if(null == chatType){
                    // command
                    switch (command) {
                        case "connect", "disconnect", "exit"  -> 
                            ChatManager.peerCommand(userCommand);
                        case "join", "delete", "leave" ->
                            ChatManager.roomCommand(userCommand);
                        default ->  
                            throw new InvalidCommand();
                    }
                }else switch (chatType) {
                    case PEER -> {
                    }
                    case ROOM -> {
                    }
                    default -> {
                        // command
                        switch (command) {
                            case "connect", "disconnect", "exit"  ->
                                ChatManager.peerCommand(userCommand);
                            case "join", "delete", "leave" ->
                                ChatManager.roomCommand(userCommand);
                            default ->
                                throw new InvalidCommand();
                        }
                    }
                }
                // send message to chat server to forward it to peer
                // send message to chat server to forward it to room
                
            }
        } catch (Exception e) {
            System.err.println("Client Error: " + e.getMessage());
        }
    }

}
