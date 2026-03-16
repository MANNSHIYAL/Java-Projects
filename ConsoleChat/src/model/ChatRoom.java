package model;

import java.util.HashSet;

public class ChatRoom {
    public HashSet<String> members;

    public ChatRoom() {
        members = new HashSet<>();
    }

    private boolean isMember(String member){
        return members.contains(member);
    }
    public synchronized  void addMember(String member){
        if(!isMember(member)) {
            members.add(member);
        }
    }

    public synchronized  void removeMember(String member){
        if(isMember(member)){
            members.remove(member);
        }
    }
}
