public interface SemaFace {

    void acquire() throws InterruptedException;
    void release();
}
