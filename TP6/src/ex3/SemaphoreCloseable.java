package ex3;

import java.nio.channels.AsynchronousCloseException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class SemaphoreCloseable {
	private final ReentrantLock lock = new ReentrantLock();
	private final Condition condition = lock.newCondition();
	private int permis;
	private int waitingThread;
	private boolean closed;

	public SemaphoreCloseable(int nbPermis) {
		if(nbPermis <0) {
			throw new IllegalArgumentException();
		}
		permis = nbPermis;
	}

	public void release() {
		lock.lock();
		try {
			if(closed) {
				return;
			}
			permis++;	
			condition.signal();
		}finally {
			lock.unlock();
		}
	}

	private boolean tryAcquire() {
		lock.lock();
		try {
			if(closed) {
				throw new IllegalStateException();
			}
			if(permis <=0) {
				waitingThread++;
				return false;
			}
			permis--;
			waitingThread--;
			return true;
		}finally {
			lock.unlock();
		}
	}

	public void acquire() throws InterruptedException, AsynchronousCloseException {
		lock.lock();
		try {
			if(closed) {
				throw new IllegalStateException();
			}
			while(permis <=0) {
				if(closed) {
					throw new AsynchronousCloseException();
				}
				condition.await();
			}
		}finally {
			lock.unlock();
		}
	}

	private int waitingForPermits() {
		lock.lock();
		try {
			return waitingThread;
		}finally {
			lock.unlock();
		}
	}

	public void close() {
		lock.lock();
		try {
			closed = true;
		} finally {
			lock.unlock();
		}
	}

	public static void main(String[] args) throws InterruptedException, AsynchronousCloseException {
		var semaphore = new SemaphoreCloseable(5);
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
		Thread.sleep(10_000);
		for(;;) {
//			System.out.println("Threads en attente "+semaphore.waitingForPermits());
			if (semaphore.waitingForPermits() == 0){
				semaphore.close();
			}
		}
	}
}


/**
 * Quel est le problème avec le code proposé ? Comment faire évoluer la classe SemaphoreClosable pour pouvoir obtenir cette fonctionnalité ? Répondez en commentaire dans la classe SemaphoreClosable.
 * 
 */
