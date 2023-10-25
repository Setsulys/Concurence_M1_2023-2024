package ex1;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


public class UnboundedSafeQueue<V> {
	
	private final ArrayList<V> queue = new ArrayList<>();
	private final ReentrantLock lock = new ReentrantLock();
	private final Condition condition = lock.newCondition();
	
	
	public void add(V value) {
		lock.lock();
		try {
			queue.addLast(value);
			condition.signal();
		}finally {
			lock.unlock();
		}
	}
	
	public V take() throws InterruptedException {
		lock.lock();
		try {
			while(queue.isEmpty()) {
				condition.await();
			}
			return queue.removeFirst();
		}finally {
			lock.unlock();
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



/**
 * Pourquoi utilise-t-on notifyAll et non pas notify ici ?
 * On utilse notifyAll et non pas notify parce que comme nous avons plusieurs Thread qui sont en attentes avec les wait
 * et si on utilise que notify on ne sait pas quel thread sera réveillé.
 */





