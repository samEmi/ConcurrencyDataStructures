package ConcurrentStructures;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CyclicBarrier {
    private class ThreadSet {
        private boolean broken = false;
    }
    private int parties;
    private int waiting;
    private ThreadSet threadSet = new ThreadSet();
    private Runnable barrierAction;

    public CyclicBarrier(int parties) {
        if(parties < 1) throw new IllegalArgumentException();
        this.parties = parties;
        this.waiting = 0;
    }

    public CyclicBarrier(int parties, Runnable barrierAction){
        this(parties);
        this.barrierAction = barrierAction;
    }

    public synchronized void await() throws InterruptedException, BrokenBarrierException {
        checks();
        if(waiting < parties){
            try {
                hit(false, -1);
            } catch (TimeoutException e) {
                throw new UnknownError();// can't happen
            }
        }
        else { openBarrier(); }
    }

    public synchronized void await(long timeout, TimeUnit timeUnit) throws InterruptedException, BrokenBarrierException, TimeoutException {
        checks();
        long timeInMilli = getTimeInMilli(timeout, timeUnit);
        if(waiting < parties){
            hit(true, timeInMilli);
        }
        else { openBarrier(); }

    }

    public synchronized int getNumberWaiting() { return waiting; }

    public synchronized int getParties() { return parties; }

    public synchronized boolean isBroken() {
        return this.threadSet.broken;
    }

    public synchronized void reset() {
        breakBarrier();
        this.threadSet = new ThreadSet();
    }

    //Private Methods--------------------------------------------------------------------
    private void hit(boolean timed, long timeInMilli) throws InterruptedException, BrokenBarrierException, TimeoutException {
        ThreadSet threadSet = this.threadSet;
        while(true){
            try{
                if(timed) { wait(timeInMilli); }
                else { wait(); }
            }
            catch (InterruptedException e){
                breakBarrier();
                throw e;
            }
            if(threadSet.broken) {throw new BrokenBarrierException();}
            else if(threadSet != this.threadSet) { return; }
            else if(timed){
                // Thread must have timed out
                breakBarrier();
                throw new TimeoutException();
            }
        }
    }

    private void checks() throws BrokenBarrierException, InterruptedException {
        if(this.threadSet.broken){
            throw new BrokenBarrierException();
        }
        else if(Thread.interrupted()){
            breakBarrier();
            throw new InterruptedException();
        }
        waiting++;
    }
    private void breakBarrier(){
        waiting = 0;
        this.threadSet.broken = true;
        notifyAll();
    }

    private void openBarrier(){
        if(barrierAction != null) {
            barrierAction.run();
            breakBarrier();
        }
        waiting = 0;
        this.threadSet = new ThreadSet();
        notifyAll();
    }
    private long getTimeInMilli(long timeout, TimeUnit timeUnit) throws TimeoutException {
        if(timeout <= 0){
            breakBarrier();
            throw new TimeoutException();
        }
        TimeUnit time = TimeUnit.MILLISECONDS;
        return time.convert(timeout, timeUnit);
    }

}



