
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class CallEventClient {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost",9090);

        BufferedReader in = new BufferedReader(
            new InputStreamReader(socket.getInputStream())
        );
        PrintWriter out  = new PrintWriter(
            socket.getOutputStream(),true
        );

        new Thread(() -> {
            try {
                String message;
                while ((message = in.readLine()) != null) { 
                    System.err.println("Dashboard update: " + message);
                }
            } catch (Exception e) {
            }
        }).start();

        out.println("CALL-1|STARTED");
        Thread.sleep(2000);
        out.println("CALL-1|ENDED");
    }
}
