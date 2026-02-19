


public class RateLimiters {
    public static void main(String[] args) throws InterruptedException {

        FixedWindow fixedWindow = new FixedWindow(5);
        SlidingWindowLog slidingWindowLog = new SlidingWindowLog(5);
        SlidingWindowCounter slidingWindowCounter = new SlidingWindowCounter(5);
        TokenBucket tokenBucket = new TokenBucket(5, 10);
        LeakyBucket leakyBucket = new LeakyBucket(5);

        for (int i = 0; i < 20; i++) {
            printData("Fixed Window", fixedWindow.isRequestAllowed());
            printData("Sliding Window Log", slidingWindowLog.isRequestAllowed());
            printData("Sliding Window Counter", slidingWindowCounter.isRequestAllowed());
            printData("Token Bucket", tokenBucket.isRequestAllowed());
            printData("Leaky Bucket", leakyBucket.isRequestAllowed());
            System.out.println("--------------------------------");
            Thread.sleep(100);
        }
    }

    private static void printData(String name, boolean isAllowed){
        System.out.println(
            String.format("%-20s : %s",
            name,
            isAllowed ? "ALLOWED" : "REJECTED")
        );
    }
}
