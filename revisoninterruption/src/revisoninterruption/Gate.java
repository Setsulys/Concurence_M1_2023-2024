package revisoninterruption;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class Gate {

	private final int maxThread;
	private ArrayList<Thread> arrived;
	private final Object lock = new Object();
	
	public Gate(int nbThread) {
		if(nbThread < 0) {
			throw new IllegalArgumentException();
		}
		this.maxThread = nbThread;
		this.arrived = new ArrayList<>();
	}
	
	public void waitAt() throws InterruptedException {
		synchronized(lock) {
			arrived.add(Thread.currentThread());
			while(maxThread != arrived.size()) {
				try {
					lock.wait();
				} catch (InterruptedException e) {
					arrived.forEach(t -> t.interrupt());
					throw new InterruptedException();
				}
				if(Thread.currentThread().isInterrupted()) {
					throw new InterruptedException();
				}
			}
			lock.notifyAll();
		}
	}
	
	public static void main(String[] args) {
	    var nbThreads = 100;
	    var tab = new Thread[nbThreads];

	    var barrier = new Gate(nbThreads);

	    IntStream.range(0, nbThreads).forEach(i -> {
	      tab[i] = Thread.ofPlatform().start(() -> {
	        try {
	          Thread.sleep(105);
	          barrier.waitAt();
	          System.out.print(i + " ");
	        } catch (InterruptedException e) {
	        	System.out.println("interrupt");
	          return;
	        }
	      });
	    });

	    try {
	      Thread.sleep(100);
	      tab[50].interrupt();
	    } catch (InterruptedException e) {
	      throw new AssertionError(e);
	    }
	  }
}
