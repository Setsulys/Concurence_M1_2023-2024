package ex3;

public class BlockingMaximum {
	private int max_value;
	private final Object lock = new Object();

	public void checker(int value) {
		synchronized(lock) {
			if(max_value < value) {
				max_value = value;
			}
			lock.notify();
		}
	}

	public int get() throws InterruptedException {
		synchronized (lock) {
			while(max_value < 8_000) {
				lock.wait();
			}
			return max_value;
		}
	}
}

