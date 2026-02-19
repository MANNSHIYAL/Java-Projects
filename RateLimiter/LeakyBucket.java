public class LeakyBucket {
    private final int maximumAllowedRequest;
    private int water;
    private long  lastLeak;

    public LeakyBucket(int maximumAllowedRequest){
        this.maximumAllowedRequest = maximumAllowedRequest;
        this.lastLeak = System.currentTimeMillis(); 
    }

    public synchronized boolean isRequestAllowed(){
        leakWater();

        if(this.water < maximumAllowedRequest){
            water++;
            return true;
        }

        return false;
    }

    private void leakWater() {
        long currentLask = System.currentTimeMillis();

        long remaining = currentLask - this.lastLeak;

        int leakWater = (int) (remaining/1000.0);

        if(leakWater > 0){
            this.water = Math.max(0, water - leakWater);
            this.lastLeak = currentLask;
        }
    }
}
