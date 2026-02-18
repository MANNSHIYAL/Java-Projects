
import java.util.LinkedList;
import java.util.Queue;

public class SlidingWindowLog {

    private final int maximumAllowedRequest;
    private final Queue<Long> timeStamps = new LinkedList<>();

    public SlidingWindowLog(int maximumAllowedRequest){
        this.maximumAllowedRequest = maximumAllowedRequest;
    }

    public synchronized boolean isRequestAllowed(){
        long currentTime = System.currentTimeMillis();

        while (!timeStamps.isEmpty() && (currentTime - timeStamps.peek() > 1000)) { 
            timeStamps.poll();
        }

        if(timeStamps.size() < this.maximumAllowedRequest){
            timeStamps.add(currentTime);
            return true;
        }
        
        return false;
    }
}
