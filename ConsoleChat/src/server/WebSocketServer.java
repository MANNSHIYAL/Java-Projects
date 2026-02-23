package server;

import connection.SecureConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLServerSocket;

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
        try {
            // while (true) { 

            //     // new Thread(() -> handleClient(client)).start();
            // }
        } catch (Exception e) {
            System.err.println("Error connecting client. " + e.getMessage());
        }
    }
}
