package ConcurrentStructures;
import java.util.LinkedList;
import java.util.Queue;

public class Semaphore implements SemaFace {
    private int maxpermits;
    private int permitsLeft;
    private Queue<Request> threadQueue = new LinkedList<Request>();
    private boolean fair;
    private class Request{}
    public Semaphore(int permits){
        this.maxpermits = permits;
        this.permitsLeft = permits;
    }

    public Semaphore(int permits, boolean fair){
        this(permits);
        this.fair = fair;
    }


    public synchronized void acquire() throws InterruptedException {
       acquire(1);
    }
    public synchronized void acquire(int permits)throws InterruptedException{
        if(permits < 0)throw new IllegalArgumentException();
        if(Thread.interrupted()) throw new InterruptedException();
        Request request = new Request();
        if(fair){
            threadQueue.add(request);
        }
        else{
            request = null;
        }
        checkWait(permits, request);
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

    public synchronized int availablePermits(){
        return permitsLeft;
    }

    public synchronized int drainPermits(){
        int result = permitsLeft;
        permitsLeft = 0;
        return result;
    }
    public synchronized void reducePermits(int reduction){
        int temp = permitsLeft - reduction;
        if(temp < 0)throw new IllegalArgumentException();
        permitsLeft = temp;
    }

    public synchronized boolean isFair(){
        return fair;
    }

    public synchronized String toString(){
        return this + "[Permits = " + permitsLeft + " ]";
    }

    private void checkWait(int permits, Request request)throws InterruptedException{
        while (permitsLeft - permits < 0 && threadQueue.peek() == request){
            wait();
        }
        threadQueue.poll();
        permitsLeft = permitsLeft - permits;
    }
}
