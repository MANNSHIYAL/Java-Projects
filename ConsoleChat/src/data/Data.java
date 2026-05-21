package data;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import javax.net.ssl.SSLSocket;

// This will only be accessible to the server to maintaing connections between clients.
public class Data {
    private static final ConcurrentHashMap<UUID,SSLSocket> activeClients = new ConcurrentHashMap<>();
    // Have to make this a coucurrent hashmap of string,socket so the the message can be forwarded to another socket.
    private static final Set<String> peerConnection = ConcurrentHashMap.newKeySet();
    
    private static final ConcurrentHashMap<String, List<String>> roomChat = new ConcurrentHashMap<>();


    public static void setPeerConnection(UUID peer1, UUID peer2){
        String key = createPeerConnectionKey(peer1, peer2);
        if(activeClients.containsKey(peer1) && activeClients.containsKey(peer2) && !peerConnection.contains(key)){
            peerConnection.add(key);
        }
    }
    public static void removePeerConnection(String key){
        peerConnection.removeIf(connection -> connection.equals(key));
    }

    public static boolean isPeerConnection(String key){
        return peerConnection.contains(key);
    }

    public static String getPeerConnectionKey(UUID peer1, UUID peer2){
        return createPeerConnectionKey(peer1, peer2);
    }

    private static String createPeerConnectionKey(UUID peer1, UUID peer2){
        String key = (peer1.compareTo(peer2) > 0 ? (peer1 + "@" + peer2) : (peer2 + "@" + peer1));
        return key;
    }

    public static void removeAllPeerConnection(String peer){
        // Remove peer from the 1-0-1 connection
        peerConnection.removeIf(connection -> connection.contains(peer));
        
        // Clean up rooms
        roomChat.forEach((roomName, members) -> {
            members.removeIf(m -> m.equals(peer));
        });
    } 

    public static void cleanRoomConnections(){
        // Will do later when will add the room chat functionality
    }

    public static boolean isClientActive(String peer){
        return activeClients.contains(peer);
    }

    public static void addNewActiveClient(UUID peer,SSLSocket socket){
        if(!activeClients.contains(peer)) activeClients.put(peer, socket);
    }

    public static SSLSocket getActiveClientConnection(UUID peer){
        return activeClients.getOrDefault(peer, null);
    }
}
