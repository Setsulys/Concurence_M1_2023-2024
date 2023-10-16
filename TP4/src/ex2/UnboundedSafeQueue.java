package ex2;

import java.util.ArrayDeque;
import java.util.Objects;

public class UnboundedSafeQueue<V> {
	
	private final ArrayDeque<V> queue = new ArrayDeque<>();
	private final Object lock = new Object();
	
	public void add(V value) {
		Objects.requireNonNull(value);
		synchronized(lock) {
			queue.addLast(value);
			lock.notify();
		}
	}

	public V take() throws InterruptedException {
		synchronized(lock) {
			while(queue.isEmpty()) {
				lock.wait();
			}
			return queue.removeFirst();
		}
	}
	
	
	public static void main(String[] args) throws InterruptedException {
		var nbThread = 3;
		var threads = new Thread[nbThread];
		var uSQ = new UnboundedSafeQueue<>();
		for(var i =0; i <nbThread;i++) {
			threads[i]=Thread.ofPlatform().start(()->{
				for(;;) {
					try {
						Thread.sleep(2000);	
					} catch (InterruptedException e) {
						throw new AssertionError(e);
					}
					uSQ.add(Thread.currentThread().getName());
				}				
			});
		}
		
		for(;;) {
			System.out.println(uSQ.take());
		}
		
	}
}
