package model;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ServerUser {
    private UUID id;
    private String client;
    private ConcurrentLinkedQueue<Message> messages;
    public ServerUser(UUID id, String client){
        this.id = id;
        this.client = client;
        this.messages = new ConcurrentLinkedQueue<Message>();
    }

    public void pushMessageToQueue(Message message){
        messages.add(message);
    }

    public boolean isMessageQueued(){
        return !messages.isEmpty();
    }

    public String getClient() {
        return client;
    }

    public UUID getId() {
        return id;
    }

    public Message getMessage() {
        Message message = messages.poll();
        return message;
    }
}
