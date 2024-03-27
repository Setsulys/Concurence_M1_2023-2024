package fr.uge.exam.exo1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Sack {

	private int maxWeight;
	private final ArrayList<Integer> hotte;
	private ReentrantLock lock = new ReentrantLock();
	private Condition condition1 = lock.newCondition();
	private Condition condition2 = lock.newCondition();
	private int weightNeeded=0;
	
	public Sack(int maxWeight) {
		if(maxWeight <0) {
			throw new IllegalArgumentException();
		}
		this.maxWeight= maxWeight;
		this.hotte = new ArrayList<>();
	}
	public void putGift(int weight) throws InterruptedException {
		lock.lock();
		try {
			while(hotte.stream().mapToInt(e->e).sum()+weight > maxWeight) {
				condition1.await();
			}
			hotte.add(weight);
			condition2.signalAll();
		}finally {
			lock.unlock();
		}
	}
	
	
	public int takeGift() throws InterruptedException {
		lock.lock();
		try{
			while(hotte.stream().mapToInt(e->e).sum() <=0) {
				condition2.await();
			}
			var taken = hotte.removeLast();
			condition1.signalAll();
			return taken;
		}finally {
			lock.unlock();
		}
	}
	
	public List<Integer> takeGiftsUntil(int weight) throws InterruptedException{
		var list = new ArrayList<Integer>();
		lock.lock();
		try{
			weightNeeded+=weight;
			while(list.stream().mapToInt(e->e).sum() < weight) {
				while(hotte.stream().mapToInt(e->e).sum() <=0) {
					condition2.await();
				}
				var last = hotte.removeLast();
				weightNeeded-=last;
				list.add(last);
				condition1.signalAll();
			}
			return list;
		}finally {
			lock.unlock();
		}
	}
	
	public int weightNeeded() {
		lock.lock();
		try {
			return weightNeeded;
		}finally{
			lock.unlock();
		}
	}
}
