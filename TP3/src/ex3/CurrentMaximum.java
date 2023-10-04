package ex3;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class CurrentMaximum {
	private Optional<Integer> max_value = Optional.empty();
	private Optional<Thread> maxThread = Optional.empty();
	private final Object lock = new Object();

	public void checker(int value,Thread thread) {
		synchronized(lock) {
			//max_value =max_value.isEmpty()?Optional.of(value):max_value.get()<value?Optional.of(value):max_value; 
			//maxThread = max_value.isEmpty()?Optional.of(thread):max_value.get()<value?Optional.of(thread):maxThread;
			if(max_value.isEmpty()) {
				max_value = Optional.of(value);
				maxThread = Optional.of(thread); 
			}
			else {
				if(max_value.get() < value) {
					max_value = Optional.of(value);
					maxThread = Optional.of(thread); 
				}
			}
			
		}
	}

	public Optional<Integer> get() {
		synchronized (lock) {
			return max_value;
		}
	}
	
	public Optional<Thread> getThread() {
		synchronized (lock) {
			return maxThread;
		}
	}


	public static void main(String[] args) throws InterruptedException {
		var cMax = new CurrentMaximum();
		var nbThreads = 4;
		int MAX_VALUE = 10_000;
		int loop =10;
		var threads = new Thread[nbThreads+1]; 

		for(var i=0; i<nbThreads;i++) {
			threads[i]=Thread.ofPlatform().start(()->{
				try {
					for(var j = 0;j<loop;j++) {
						Thread.sleep(1000);
						var value = ThreadLocalRandom.current().nextInt(MAX_VALUE);
						cMax.checker(value,Thread.currentThread());
						System.out.println("Thread " +Thread.currentThread() +" propose " + value);
					}
				} catch (InterruptedException e) {
					return;
				}
			});
		}
		threads[nbThreads] = Thread.ofPlatform().start(()->{
			for(var i=0; i< loop; i++) {
				try {
					Thread.sleep(1000);
					if(!cMax.get().isEmpty()) {
						System.out.println("Max courant : "+cMax.get().get());
					}
				} catch (InterruptedException e) {
					return;
				}

			}
		});

		for(var thread : threads) {
			thread.join();
		}
		if(!cMax.get().isEmpty() && ! cMax.getThread().isEmpty()) {
			System.out.println("Max final : "+ cMax.get().get());
			System.out.println("Thread Max : "+cMax.getThread().get());
		}
	}
}

/**
 * 
 */
