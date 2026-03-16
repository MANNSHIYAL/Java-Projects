package server;

import commands.InvalidCommand;
import java.io.InputStream;
import java.io.OutputStream;
import javax.net.ssl.SSLSocket;
import model.ChatType;
import websocket.WebSocketDecoder;
import websocket.WebSocketHandShake;

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
            WebSocketDecoder decoder = new WebSocketDecoder();
            // Websocket Server to ChatServer
            while (true) { 
                String messageReceived = decoder.decodeMessage(in);
                String peerMessageType = "\"type\": " + ChatType.PEER.name();
                String roomMessageType = "\"type\": " + ChatType.ROOM.name();
                String command = "";
                String userCommand = "";
                if(messageReceived.contains(peerMessageType)){
                    // send message to chat server to forward it to peer

                }else if(messageReceived.contains(roomMessageType)){
                    // send message to chat server to forward it to room
                }else{
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
        } catch (Exception e) {
            System.err.println("Client Error: " + e.getMessage());
        }
    }

}
