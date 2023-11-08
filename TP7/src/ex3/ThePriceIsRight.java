package ex3;

import java.util.LinkedHashMap;

public class ThePriceIsRight {

	private final int realPrice;
	private final int nbThreads;
	private int cpt;
	private final Object lock = new Object();
	private final LinkedHashMap<Thread, Integer> propositions;


	public ThePriceIsRight(int price, int nbThreads) {
		if(price <0 || nbThreads < 0) {
			throw new IllegalArgumentException();		
		}
		synchronized(lock) {
			this.realPrice = price;
			this.nbThreads = nbThreads;
			this.cpt=0;
			this.propositions =  new LinkedHashMap<>();
		}
	}

	public boolean propose(int value) throws InterruptedException {
		synchronized(lock) {
			cpt++;
			if(cpt> nbThreads) {
				return false;
			}
			propositions.put(Thread.currentThread(), value);
			if(cpt==nbThreads) {
				lock.notifyAll();
			}
			
			while(cpt <= nbThreads) {
				lock.wait();
			}
			System.out.println(nearest());
			if(Thread.currentThread().equals(nearest())){
				return true;
			}
		}
		return false;
	}
	
	public Thread nearest() {
		synchronized(lock) {
			return propositions.entrySet().stream()
					.min((e1,e2) -> Integer.compare(distance(e1.getValue()),distance(e2.getValue()))).get().getKey();
		}
	}
	
	private int distance(int price) {
		  return Math.abs(price - realPrice);
		}
	
	public static void main(String[] args) {
		ThePriceIsRight tpir = new ThePriceIsRight(100, 2);
	    Thread thread = Thread.ofPlatform().start(() -> 
	    {
			try {
				tpir.propose(102);
				tpir.propose(97);
			} catch (InterruptedException e) {
				return;
			}
		});
	    
	}
}
