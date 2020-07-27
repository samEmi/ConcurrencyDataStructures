package ConcurrentStructures;

import java.util.concurrent.TimeUnit;

public class CountDownLatch {
    private long count;
    private boolean notified = false;
    public CountDownLatch(int count){
        this.count = count;
    }

    public synchronized void await() throws InterruptedException{
        await(true, 0);
    }
    public synchronized boolean await(long timeout, TimeUnit unit) throws InterruptedException{
        long timeInMilli = unit.toMillis(timeout);
        return await(true,timeInMilli);
    }

    public synchronized void countDown(){
        if(count > 0){
            count--;
        }
        else{
            notifyAll();
            notified = true;
        }
    }

    public synchronized long getCount(){
        return count;
    }

    public synchronized String toString(){
        return super.toString() + "[Count = " + count + " ]";
    }

    private boolean await(boolean timed, long timeInMilli)throws InterruptedException{
        if(Thread.interrupted()) throw new InterruptedException();
        while (count > 0){
            if(timed){
                wait(timeInMilli);
                if(!notified)return false;
            }
            else {
                wait();
            }
        }
        return true;
    }


}
