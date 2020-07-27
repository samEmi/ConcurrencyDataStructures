

public class Semaphore implements SemaFace {
    private int maxpermits;
    private int permitsLeft;
    private class Request{
        int requestedPermits;
    }
    public Semaphore(int permits){
        this.maxpermits = permits;
        this.permitsLeft = permits;
    }


    public synchronized void acquire() throws InterruptedException {
       acquire(1);
    }
    public synchronized void acquire(int permit)throws InterruptedException{
        if(Thread.interrupted()) throw new InterruptedException();
        while (permitsLeft - permit < 0){
            wait();
        }
        permitsLeft = permitsLeft - permit;
    }


    public synchronized void release() {
        release(1);
    }

    public synchronized void release(int permits){
        if(permits < 0)throw new IllegalArgumentException();
        int temp = permitsLeft + permits;
        permitsLeft = (temp > maxpermits)? maxpermits : temp;
        notifyAll();
    }


}
