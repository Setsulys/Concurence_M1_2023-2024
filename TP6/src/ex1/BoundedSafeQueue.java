package ex1;

import java.util.ArrayDeque;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


public class BoundedSafeQueue<V> {
	private final int SIZE;
	private final ArrayDeque<V> queue;
	private final ReentrantLock lock = new ReentrantLock();
	private final Condition condition = lock.newCondition();
	private final Condition conditionb = lock.newCondition();
	
	public BoundedSafeQueue(int size){
		if(size < 0) {
			throw new IllegalArgumentException();
		}
		this.SIZE = size;
		this.queue = new ArrayDeque<>(SIZE);
	}
	
	public void put(V value) throws InterruptedException {
		lock.lock();
		try {
			queue.addLast(value);
			while(queue.size() ==SIZE) {
				condition.await();
			}
			conditionb.signal();
		}finally {
			lock.unlock();
		}
	}
	
	public V take() throws InterruptedException {
		lock.lock();
		try {
			while(queue.isEmpty()) {
				conditionb.await();
			}
			var element = queue.poll();
			condition.signal();
			return element;
		}finally {
			lock.unlock();
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		var nbThread = 10	;
		var threads = new Thread[nbThread];
		var bSQ = new BoundedSafeQueue<>(2);
		for(var i =0; i <nbThread;i++) {
			threads[i]=Thread.ofPlatform().start(()->{
				for(;;) {
					try {
						Thread.sleep(2000);	
					} catch (InterruptedException e) {
						throw new AssertionError(e);
					}
					try {
					bSQ.put(Thread.currentThread().getName());
					}catch(InterruptedException e) {
						throw new AssertionError(e);
					}
				}				
			});
		}
		
		for(;;) {
			System.out.println(bSQ.take());
		}
	}
}
