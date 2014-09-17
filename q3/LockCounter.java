package q3;

// TODO
// Use MyLock to protect the count

public class LockCounter extends Counter {
	MyLock lock;
    public LockCounter(MyLock lock) {
    	this.lock = lock;
    }

    @Override
    public void increment() {
    	count++;
    }

    @Override
    public int getCount() {
        return count;
    }
}
