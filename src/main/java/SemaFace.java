public interface SemaFace {

    void acquire() throws InterruptedException;
    void acquire(int permit) throws InterruptedException;
    void release();
    void release(int permit) throws InterruptedException;
}
