package ex1;

import java.util.concurrent.ArrayBlockingQueue;

public class Codex {

	public static void main(String[] args) throws InterruptedException {
		var nbThreadReceive =3;
		var nbThreadDcode =2;
		var nbThreadarchive =1;
		var nbThreads = nbThreadReceive+nbThreadDcode+nbThreadarchive;
		var threads = new Thread[nbThreads];
		var queue = new ArrayBlockingQueue<String>(10);
		var queue2 = new ArrayBlockingQueue<String>(10);
		
		for(var i=0; i < nbThreadReceive;i++) {
			threads[i]=Thread.ofPlatform().start(()->{
				for(;;) {
					try {
						var recieved = CodeAPI.receive();
						System.out.println(recieved);
						queue.put(recieved);
					} catch (InterruptedException e) {
						throw new AssertionError(e);
					}
				}
			});
		}
		for(var i=0; i < nbThreadDcode;i++) {
			threads[nbThreadReceive +i]=Thread.ofPlatform().start(()->{
				for(;;) {
					try {
						var toDecode = CodeAPI.decode(queue.take());
						System.out.println(toDecode);
						queue2.put(toDecode);
					} catch (InterruptedException e) {
						throw new AssertionError(e);
					} catch (IllegalArgumentException e) {
					}
				}
			});
		}
		threads[nbThreads-1]=Thread.ofPlatform().start(()->{
			for(;;) {
				try {
					CodeAPI.archive(queue2.take());
				} catch (InterruptedException e) {
				}
			}
		});
		for(var i=0; i < nbThreads;i++) {
			threads[i].join();
		}
		
	}
}
