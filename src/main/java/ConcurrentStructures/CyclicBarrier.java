package ConcurrentStructures;


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
}



