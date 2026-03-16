package data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


public class Data {
    private static final HashSet<String> peerConnection = new HashSet<>();
    private static final HashMap<String, ArrayList<String>> roomChat = new HashMap<>();

    public static synchronized void setPeerConnection(String peer1, String peer2){
        String key = createPeerConnectionKey(peer1, peer2);
        if(!peerConnection.contains(key)){
            peerConnection.add(key);
        }
    }
    public static synchronized void removePeerConnection(String key){
        if (isPeerConnection(key)) {
            peerConnection.remove(key);
        }
    }

    public static synchronized boolean isPeerConnection(String key){
        return peerConnection.contains(key);
    }

    public static synchronized String getPeerConnectionKey(String peer1, String peer2){
        return createPeerConnectionKey(peer1, peer2);
    }

    private static String createPeerConnectionKey(String peer1, String peer2){
        String key = (peer1.compareTo(peer2) > 0 ? (peer1 + "@" + peer2) : (peer2 + "@" + peer1));
        return key;
    }

    public static synchronized void removeAllPeerConnection(String peer){
        ArrayList<String> keys = new ArrayList<>();
        peerConnection.forEach((connection) -> {
            if(connection.contains(peer)){
                keys.add(connection);
            }
        });
        peerConnection.removeAll(keys);
        removeConnections(peer);
    } 

    private static CompletableFuture<Void> removeConnections(String peer) {
    for (Map.Entry<String, ArrayList<String>> room : roomChat.entrySet()) {
        room.getValue().remove(peer);
    }
    return CompletableFuture.completedFuture(null);
}

}
