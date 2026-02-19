

public class TokenBucket {
    private final int maximumAllowedRequest;
    private final int refillRate;
    private long tokens;
    private long lastRefillTime;

    public TokenBucket(int maximumAllowedRequest,int refillRate){
        this.maximumAllowedRequest = maximumAllowedRequest;
        this.refillRate = refillRate;
        this.tokens = maximumAllowedRequest;
        this.lastRefillTime = System.currentTimeMillis();
    }

    public synchronized boolean isRequestAllowed(){
        refillBucket();

        if(this.tokens >= 1){
            this.tokens--;
            return true;
        }

        return false;
    }

    private void refillBucket() {
        long currentTime = System.currentTimeMillis();

        double seconds = (currentTime - this.lastRefillTime)/1000.0;

        this.tokens = Math.min(this.maximumAllowedRequest, this.tokens + ((int) seconds*this.refillRate));

        this.lastRefillTime = currentTime;
    }
}
