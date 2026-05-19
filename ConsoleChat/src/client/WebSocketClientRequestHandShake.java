package client;

import connection.SecureConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.net.ssl.SSLSocket;

public class WebSocketClientRequestHandShake {
    public SSLSocket requestHandShake() throws IOException,NoSuchAlgorithmException, KeyManagementException, UnknownHostException {
        SecureConnection secureConnection = new SecureConnection();
        SSLSocket socket = secureConnection.byPassSecurityCheckForClient();
        socket = requestProtocolUpgrade(socket);
        return socket;
    }

    private SSLSocket requestProtocolUpgrade(SSLSocket socket) throws IOException {
        // 1. WebSocket Handshake Request
        String key = Base64.getEncoder().encodeToString(new byte[16]);
        String upgradeProtocolRequest = "GET / HTTP/1.1\r\n"
                                        + "Host: localhost\r\n"
                                        + "Upgrade: websocket\r\n"
                                        + "Connection: Upgrade\r\n"
                                        + "Sec-WebSocket-Key: " + key + "\r\n"
                                        + "Sec-WebSocket-Version: 13\r\n\r\n";
        socket.getOutputStream().write(upgradeProtocolRequest.getBytes());
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String line;
        while (!(line = reader.readLine()).isEmpty());

        return socket;
    }
}
