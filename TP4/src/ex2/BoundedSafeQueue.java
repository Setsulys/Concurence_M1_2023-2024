package ex2;

import java.util.ArrayDeque;
import java.util.Objects;

public class BoundedSafeQueue<V> {
	private int size;
	private final ArrayDeque<V> queue;
	private final Object lock = new Object();

	public BoundedSafeQueue(int size) {
		if(size < 1) {
			throw new IllegalArgumentException();
		}
		this.size = size;
		queue = new ArrayDeque<>();
	}

	public void put(V value) throws InterruptedException {
		Objects.requireNonNull(value);
		synchronized (lock) {
			while(queue.size() >size) {
				lock.wait();
			}
			queue.add(value);
			lock.notifyAll();
		}
	}

	public V take () throws InterruptedException {
		synchronized (lock) {
			while(queue.isEmpty()) {
				lock.wait();
			}
			var val = queue.removeFirst();
			lock.notifyAll();
			return val;
		}
	}

	public static void main(String[] args) throws InterruptedException {
		var nbThread = 3;
		var threads = new Thread[nbThread];
		var bSQ = new BoundedSafeQueue<>(10);
		for(var i =0; i <nbThread;i++) {
			threads[i]=Thread.ofPlatform().start(()->{
				for(;;) {
					try {
						Thread.sleep(2000);
						bSQ.put(Thread.currentThread().getName());
					} catch (InterruptedException e) {
						return;
					}
				}				
			});
		}

		for(;;) {
			System.out.println(bSQ.take());
		}

	}
}
