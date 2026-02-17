public class CallEvent{
    public String callId;
    public String status;
    public long timeStamp;

    public CallEvent(String callId, String status){
        this.callId = callId;
        this.status = status;
        this.timeStamp = System.currentTimeMillis();
    }
    @Override
    public String toString(){
        return callId + "|" + status + "|" + timeStamp;
    }
}