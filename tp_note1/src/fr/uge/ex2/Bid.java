package fr.uge.ex2;

import java.util.ArrayList;
import java.util.Optional;

public class Bid {

	private final ArrayList<Integer> list= new ArrayList<>();
	private final Object lock = new Object();
	private final int max =5;
	
	public Optional<Integer> propose(int value) throws InterruptedException {
		synchronized(lock) { 
			System.out.println("Thread :" + Thread.currentThread().getName() + " propose " +value);
			if(list.contains(value)) {
				System.out.println("Thread :" + Thread.currentThread().getName() + " proposed value " +value+" was rejected");
				return Optional.empty();
			}
			
			list.add(value);
			if(list.size()==max) {
				lock.notifyAll();
			}
			
			while(list.size()<max) {
				lock.wait();
			}
			
			while(value!=smallest()) {
				lock.wait();
			}
			
			if(smallest()==value) {
				System.out.println("Thread :" + Thread.currentThread().getName() + " was unblocked because its proposed value " +value+" is now the smallest");
				
				list.removeIf(e -> (e==smallest()));
				return Optional.of(getSmallGreatThanAverage(average()));
			}
			return Optional.empty();
		}
	}
	
	
	
	public int smallest() {
		synchronized(lock) {
			return list.stream().mapToInt(e-> e).min().getAsInt();
		}
		
	}
	
	public int average() {
		synchronized(lock) {
			return list.stream().mapToInt(Integer::valueOf).sum()/5;
		}
		
	}
	
	public int getSmallGreatThanAverage(int avg) {
		synchronized(lock) {
			return list.stream().filter(e -> (e>avg)).mapToInt(i->i).min().getAsInt()+1;
		}
		
	}
	
}
