package ecom.test.request.limiter;

public class CircularTimeChecker {

    private final long[] queue;
    private final int maxSize;
    private final long timeLimit;
    private int head;

    public CircularTimeChecker(int maxSize, long timeLimit) {
        this.maxSize = maxSize;
        this.timeLimit = timeLimit;
        this.queue = new long[maxSize];
    }

    public synchronized boolean check() {
        final long curTimestamp = System.currentTimeMillis();
        final long oldestTimestamp = getOldestTimestamp();

        if (oldestTimestamp > curTimestamp - timeLimit) {
            return false;
        }

        head = ++head % maxSize;
        queue[head] = curTimestamp;
        return true;
    }

    public long getOldestTimestamp() {
        return queue[(head + 1) % maxSize];
    }
}
