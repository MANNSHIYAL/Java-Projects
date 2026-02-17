public class FixedWindow {

    private final int maximumAllowedRequest;
    private long windowStartTime; 
    private int currentProcessingRequests;

    public FixedWindow(int maximumAllowedRequest){
        this.maximumAllowedRequest = maximumAllowedRequest;
        this.windowStartTime = System.currentTimeMillis();
        this.currentProcessingRequests = 0;
    }

    public boolean isRequestAllowed(){
        long currentTime = System.currentTimeMillis();

        if(currentTime - this.windowStartTime >= 1000){
            this.windowStartTime = currentTime;
            this.currentProcessingRequests = 0;
        }

        if(this.currentProcessingRequests < this.maximumAllowedRequest){
            this.currentProcessingRequests++;
            return true;
        }

        return false;
    }
}
