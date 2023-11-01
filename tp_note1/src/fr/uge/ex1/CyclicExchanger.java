package fr.uge.ex1;

import java.util.ArrayList;

public class CyclicExchanger<T> {

	private final int nbMax;
	private final Object lock = new Object();
	private final ArrayList<T> list = new ArrayList<>();
	
	public CyclicExchanger(int nbParticipant){
		if(nbParticipant < 0) {
			throw new IllegalArgumentException();
		}
		synchronized(lock) {
			this.nbMax= nbParticipant;
		}
		
	}
	public T exchange(T value) throws InterruptedException {
		synchronized(lock) {
			int cpt = list.size();
			list.add(value);
			
			if(list.size()==nbMax) {
				lock.notifyAll();
			}
			while(list.size() < nbMax) {
				lock.wait();
			}
			if(cpt==nbMax-1) {
				return list.get(0);
			}
			return list.get(cpt+1);

		}
		
	}
	
	public static void main(String[] args) {
		var nbThread = 5;
		var exchanger = new CyclicExchanger<Integer>(5);
		for(var i= 0;i < nbThread; i++) {
			var j = i;
			Thread.ofPlatform().name(j+"").start(()->{
				try {
					Thread.sleep(j*1_000);
				} catch (InterruptedException e) {
					throw new AssertionError(e);
				}
				try {
					System.out.println("Thread :" + Thread.currentThread().getName() +" => "+exchanger.exchange(j));
				} catch (InterruptedException e) {
					throw new AssertionError(e);
				}
			});
		}
	}
}
