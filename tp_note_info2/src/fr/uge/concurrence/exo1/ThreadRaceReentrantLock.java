package fr.uge.concurrence.exo1;

import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadRaceReentrantLock {
	private final ReentrantLock lock = new ReentrantLock();
	private final int nbParticipants;
	private final HashMap<Thread,Integer> participants;

	public ThreadRaceReentrantLock(int nbParticipants) {
		if(nbParticipants <0) {
			throw new IllegalArgumentException();
		}
		this.nbParticipants = nbParticipants;
		participants = new HashMap<>();
	}

	public boolean enlist() throws InterruptedException {
		lock.lock();
		try{
			if(!participants.containsKey(Thread.currentThread()) && participants.size() < nbParticipants) {
				participants.put(Thread.currentThread(), 0);
				while(participants.size() != nbParticipants) {
					lock.wait();
				}
				lock.notifyAll();
				return true;
			}
			return false;
		}finally {
			lock.unlock();
		}
	}
}
