package ex3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Semaphore {
	private final ReentrantLock lock = new ReentrantLock();
	private final Condition condition = lock.newCondition();
	private int permis;

	public Semaphore(int nbPermis) {
		if(nbPermis <0) {
			throw new IllegalArgumentException();
		}
		permis = nbPermis;
	}

	public void release() {
		lock.lock();
		try {
			permis++;
			condition.signal();
		}finally {
			lock.unlock();
		}
	}

	public boolean tryAcquire() {
		lock.lock();
		try {
			if(permis <=0) {
				return false;
			}
			permis--;
			return true;
		}finally {
			lock.unlock();
		}
	}

	public void acquire() throws InterruptedException {
		lock.lock();
		try {
			while(permis <=0) {
				condition.await();
			}
		}finally {
			lock.unlock();
		}
	}

	public static void main(String[] args) {
		var semaphore = new Semaphore(5);
		var nbThread = 10;
		for(var i=0; i<nbThread; i++) {
			Thread.ofPlatform().start(()->{
				for(;;) {
					try {
						semaphore.tryAcquire();
						System.out.println(Thread.currentThread().getName() + " acquire");
						Thread.sleep(1_000);
						semaphore.release();
						System.out.println(Thread.currentThread().getName() + " release");
					} catch (InterruptedException e) {
						throw new AssertionError(e);
					}
				}
			});
		}
	}
}
