package ex1;

import java.util.stream.IntStream;

public class HelloListFixedBetter {
	public static void main(String[] args) throws InterruptedException {
		var nbThreads = 4;
		var threads = new Thread[nbThreads];
		var threadSafeList = new ThreadSafeList<Integer>();
		
		
		IntStream.range(0, nbThreads).forEach(j ->{
			threads[j] = Thread.ofPlatform().start(()->{
				for(var i = 0; i <5_000; i++) {
					threadSafeList.add(i);
				}
			});
		});
		
		for(Thread thread : threads) {
			thread.join();
		}
		System.out.println("taille de la liste: "+threadSafeList.size());
		
		System.out.println(threadSafeList.toString());
	}
}
