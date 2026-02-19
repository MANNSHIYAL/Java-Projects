public class SlidingWindowCounter {
    private final int maximumAllowedRequest;
    private long windowStartTime; 
    private int currentCount;
    private int previousCount;

    public SlidingWindowCounter(int maximumAllowedRequest){
        this.maximumAllowedRequest = maximumAllowedRequest;
        this.windowStartTime = System.currentTimeMillis();
    }   

    public synchronized boolean isRequestAllowed(){
        long currentTime = System.currentTimeMillis();
        long timeSpend = currentTime - this.windowStartTime;

        if(timeSpend >= 1000){
            this.windowStartTime = currentTime;
            this.previousCount = this.currentCount;
            this.currentCount = 0;
            timeSpend = 0;
        }

        double requestRate = this.previousCount*(1 - timeSpend/1000.0) + this.currentCount;

        if(requestRate < this.maximumAllowedRequest){
            this.currentCount++;
            return true;
        }

        return false;
    }
}
