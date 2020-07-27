package ConcurrentStructures;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class ReentrantLock implements Lock, Serializable {
    private int count;
    private Thread owner;
    public ReentrantLock(){
        count = 0;
    }
    public synchronized void lock() {
        try {
            lockHelper(false);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public void lockInterruptibly() throws InterruptedException {
        lockHelper(false);
    }

    public boolean tryLock() {
        boolean result = false;
        try{
            result = lockHelper(true);
        }
        catch (InterruptedException e){
        }
        return result;
    }

    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        lockHelper(true);
        return false;
    }

    public void unlock() {
        if(Thread.currentThread() == owner){
            count--;
            if(count == 0)owner = null;
        }
        else{
            throw new IllegalMonitorStateException();
        }

    }

    public Condition newCondition() {
        return null;
    }

    private boolean lockHelper(boolean trying)throws InterruptedException {
        while (owner != null) {
            if (owner == Thread.currentThread()) {
                count++;return true;
            }
            if(!trying) {
                wait();
            }
            else {
                return false;
            }
        }
        owner = Thread.currentThread();
        count = 1;
        return true;
    }
}
