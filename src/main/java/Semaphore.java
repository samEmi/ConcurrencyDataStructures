

public class Semaphore implements SemaFace {
    private int maxpermits;
    private int permitsLeft;
    public Semaphore(int permits){
        this.maxpermits = permits;
        this.permitsLeft = permits;
    }


    public synchronized void acquire() throws InterruptedException {
        if(Thread.interrupted()) throw new InterruptedException();
        while (permitsLeft == 0){
            wait();
        }
        permitsLeft--;
    }

    public synchronized void release() {
        if(permitsLeft < maxpermits){
            permitsLeft++;
        }
        notifyAll();
    }


}
