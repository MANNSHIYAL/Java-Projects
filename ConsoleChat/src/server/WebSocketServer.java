package server;

import connection.SecureConnection;
import java.util.Map;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;
import util.JsonUtil;

// This class is responsible for the TCP secure TCP connection and assigning thread to each client and a chat room
public class WebSocketServer {
    public static void main(String[] args) throws Exception {
        // Connection instance to access properties file.
        SecureConnection connection = new SecureConnection();
        // Getting key manager factory
        KeyManagerFactory keyManagerFactory = connection.keyStore();
        // Instance of the SSL server socket for secure connection.
        SSLServerSocket serverSocket = connection.sslSocketConnection(keyManagerFactory);

        System.out.println("Websocket started at port: " + serverSocket.getLocalPort());
        // Websocket server to Websocket Client Handler 
        String message = "asassa";
        String type = "sdfsa";
        String from = "sdfas";
        String to = "sfda";
        try {
            while (true) { 
                System.out.println("{ " + 
                "\"message\": " + message +
                "\"type\": " + type + 
                "\"from\": " + from +
                (to != null ? "\"to\": " + to : "") + 
                " }");
                Map<String,Object> map = JsonUtil.jsonToMap("{ " + 
                "\"message\": " + message +
                "\"type\": " + type + 
                "\"from\": " + from +
                (to != null ? "\"to\": " + to : "") + 
                " }");
                for (Map.Entry<String, Object> en : map.entrySet()) {
                    Object key = en.getKey();
                    Object val = en.getValue();
                    System.out.println(key + " : " + val);
                }
                SSLSocket client = (SSLSocket)serverSocket.accept();
                new Thread(() -> WebSocketClientHandler.handleClient(client)).start();
            }
        } catch (Exception e) {
            System.err.println("Error connecting client. " + e.getMessage());
        }
    }
}
