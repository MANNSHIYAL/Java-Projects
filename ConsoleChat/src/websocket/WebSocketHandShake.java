package websocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;

// This class will help the websocket handshake between the client and the server
public class WebSocketHandShake {
    public boolean doHandShake(InputStream in,OutputStream out) throws IOException, NoSuchAlgorithmException {
        try {
            Scanner sc = new Scanner(in,"UTF-8");
            String data = sc.useDelimiter("\r\n\r\n").next();
            if(!data.contains("Sec-WebSocket-Key:")) return false;
    
            String key = data.split("Sec-WebSocket-Key:")[1].split("\r\n")[0].trim();
            String accept = Base64.getEncoder().encodeToString(
                MessageDigest.getInstance("SHA-1")
                .digest((key + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").getBytes("UTF-8"))
            );
            out.write(("HTTP/1.1 101 Switching Protocols\r\n" +
                        "Upgrade: websocket\r\n" +
                        "Connection: Upgrade\r\n" +
                        "Sec-WebSocket-Accept: " + accept + "\r\n" +
                        "\r\n"
                    ).getBytes("UTF-8"));
        } catch (IOException e) {
            System.err.println( "I/O Exception occured: " + e.getMessage());
        }
        return true ;
    }
}
