package fr.uge.exo2;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import javax.print.attribute.standard.PrinterURI;

public class SecretSanta {
	
	private final ReentrantLock lock = new ReentrantLock();
	private final Condition condition1 = lock.newCondition();
	private final Condition condition2 = lock.newCondition();
	
	private boolean interruptFlag;
	
	private final int nbParticipant;
	private final List<Integer> order;
	private final List<String> name;
	
	public SecretSanta(int nbParticipant,List<Integer> order) {
		Objects.requireNonNull(order);
		if(nbParticipant <0) {
			throw new IllegalArgumentException();
		}
		this.nbParticipant = nbParticipant;
		this.order = order;
		this.name = new ArrayList<>();
	}
	
	public String submit(String value) throws InterruptedException {
		Objects.requireNonNull(value);
		lock.lock();
		try {
			if(name.size()==nbParticipant) {
				throw new IllegalArgumentException();
			}
			name.add(value);
			condition2.signalAll();
			while(name.size()!=nbParticipant) {
				condition1.await();
			}
			condition1.signalAll();
			var ione = Integer.parseInt(Thread.currentThread().getName().replace("Thread ", ""));
			var jone = order.get(ione%nbParticipant);
			return name.get((jone)%nbParticipant);
		}finally {
			lock.unlock();
		}
	}
	
	public List<String> observe(int n) throws InterruptedException{
		lock.lock();
		try {
			if(interruptFlag) {
				throw new IllegalStateException();
			}
			while(name.size() < n) {
				if(interruptFlag) {
					Thread.currentThread().interrupt();
					throw new InterruptedException();
				}
				try {
					condition2.await();
				} catch (InterruptedException e) {
					interruptFlag=true;
					
				}
			}
			return name;
		}finally {
			lock.unlock();
		}
	}
}