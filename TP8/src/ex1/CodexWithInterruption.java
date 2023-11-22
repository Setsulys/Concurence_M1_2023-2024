package ex1;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

public class CodexWithInterruption {
	public static boolean flag = false;
	public static void main(String[] args) throws InterruptedException {
		var nbThreadReceive =3;
		var nbThreadDcode =2;
		var nbThreadarchive =1;
		var nbThreads = nbThreadReceive+nbThreadDcode+nbThreadarchive;
		var threads = new ArrayList<Thread>();
		var queue = new ArrayBlockingQueue<String>(10);
		var queue2 = new ArrayBlockingQueue<String>(10);
		
		for(var i=0; i < nbThreadReceive;i++) {
			var newThread = Thread.ofPlatform().start(()->{
				for(;!Thread.currentThread().isInterrupted();) {
					try {
						var recieved = CodeAPI.receive();
						System.out.println(recieved);
						queue.put(recieved);
					} catch (InterruptedException e) {
						return;
					}
				}
			});
			threads.add(newThread);
		}
		for(var i=0; i < nbThreadDcode;i++) {
			var newThread = Thread.ofPlatform().start(()->{
				for(;!Thread.currentThread().isInterrupted();) {
					try {
						var toDecode = CodeAPI.decode(queue.take());
						System.out.println(toDecode);
						queue2.put(toDecode);
					} catch (InterruptedException e) {
						return;
					} catch (IllegalArgumentException e) {
						for(var t :threads) {
							t.interrupt();
						}
					}
				}
			});
			threads.add(newThread);
		}
		var newThread =Thread.ofPlatform().start(()->{
			for(;!Thread.currentThread().isInterrupted();) {
				try {
					CodeAPI.archive(queue2.take());
				} catch (InterruptedException e) {
					return;	
				}
			}
		});
		threads.add(newThread);
	}
}
