
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CallEventServer {
    
    private static Set<ClientHandler> clients = ConcurrentHashMap.newKeySet();
    private static Map<String,CallEvent> activeClients = new ConcurrentHashMap<String,CallEvent>();

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(9090);
        System.out.println("Server started on port 9090");

        while (true) { 
            Socket socket = serverSocket.accept();
            ClientHandler clientHandler = new ClientHandler(socket);

            clients.add(clientHandler);

            new Thread(clientHandler).start();
        }
    }    
    static void broadcast(CallEvent event){
        activeClients.put(event.callId, event);

        for (ClientHandler client : clients) {
            client.send(event.toString());
        }
    }
    
    static class ClientHandler implements Runnable {

        private Socket socket;
        private PrintWriter out;

        ClientHandler(Socket socket) throws IOException {
            this.socket = socket;
            this.out = new PrintWriter(socket.getOutputStream(),true);
        }

        public void send(String message) {
            out.println(message);
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
                );

                String line;
                while ((line = in.readLine()) != null) { 
                    String parts[] = line.split("\\|");
                    CallEvent event = new CallEvent(parts[0],parts[1]);
                    broadcast(event);
                }
            } catch (Exception e) {
                clients.remove(this);
            }
        }

    }
}
