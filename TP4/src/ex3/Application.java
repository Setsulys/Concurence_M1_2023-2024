package ex3;

import java.util.concurrent.ThreadLocalRandom;

public class Application {

	public static void main(String[] args) throws InterruptedException {
		var nbThread = 4;
		var threads= new Thread[nbThread];
		int MAX_VALUE = 10_000;
		var bM  = new BlockingMaximum();
		
		
		for(var i = 0; i< nbThread;i++) {
			threads[i] = Thread.ofPlatform().start(()->{
				for(;;) {
					try {
						Thread.sleep(1000);
					}catch(InterruptedException e) {
						throw new AssertionError(e);
					}
					var value =ThreadLocalRandom.current().nextInt(MAX_VALUE);
					bM.checker(value);
					System.out.println("Thread " + Thread.currentThread() + " a tiré " + value);
				}
			});
		}
		
		for(;;) {
			Thread.sleep(1000);
			System.out.println("Max courant : " + bM.get());
		}
	}
}
