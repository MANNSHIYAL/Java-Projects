package server;

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
            while (true) { 
                String messageReceived = decoder.decodeMessage(in);
                String peerMessageType = "\"type\": " + ChatType.PEER.name();
                if(messageReceived.contains(peerMessageType)){
                    // send message to chat server to forward it to peer
                }else{
                    // send message to chat server to forward it to room
                }
            }
        } catch (Exception e) {
            System.err.println("Client Error: " + e.getMessage());
        }
    }

}
