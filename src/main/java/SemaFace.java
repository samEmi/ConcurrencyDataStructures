
// In case I decide to write a different Semaphore implementation
public interface SemaFace {

    void acquire() throws InterruptedException;
    void acquire(int permit) throws InterruptedException;
    void release();
    void release(int permit) throws InterruptedException;
    int availablePermits();
    int drainPermits();
    void reducePermits(int reduction);
    boolean isFair();
}
