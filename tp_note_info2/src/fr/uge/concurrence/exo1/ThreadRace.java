package fr.uge.concurrence.exo1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.plaf.synth.SynthColorChooserUI;

public class ThreadRace {
	private final Object lock = new Object();
	private final int nbParticipants;
	private final HashMap<Thread,Integer> participants;
	private final int STEP;
	private final ArrayList<Thread> finished = new ArrayList<>();

	public ThreadRace(int nbParticipants,int step) {
		if(nbParticipants <0) {
			throw new IllegalArgumentException();
		}
		this.nbParticipants = nbParticipants;
		participants = new HashMap<>();
		this.STEP = step;
	}

	public boolean enlist() throws InterruptedException {
		synchronized(lock) {
			if(!participants.containsKey(Thread.currentThread()) && participants.size() < nbParticipants) {
				participants.put(Thread.currentThread(), 0);
				while(participants.size() != nbParticipants) {
					lock.wait();
				}
				lock.notifyAll();
				return true;
			}
			return false;
		}
	}
	
	public int step() throws InterruptedException {
		synchronized(lock) {
			var steps = participants.getOrDefault(Thread.currentThread(),null);
			if(steps== null || steps==STEP) {
				return -1;
			}
			else{
				steps+=1;
				var nbFinished = finished.size();
				participants.put(Thread.currentThread(), steps);
				if(steps==STEP) {
					finished.add(Thread.currentThread());
				}
				while(steps==STEP && finished.size() != nbParticipants) {
					lock.wait();
				}
				lock.notifyAll();
				return nbFinished;
			}
		}
	}
	
	public List<Thread> finish() throws InterruptedException{
		synchronized(lock) {
			while(finished.size() != nbParticipants) {
				lock.wait();
			}
			lock.notify();
			return finished;
		}

	}
}
