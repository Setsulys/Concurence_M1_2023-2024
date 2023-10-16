package ex4;

import java.util.HashMap;
import java.util.Objects;

public class Vote {

	private final int nbVoteMax;
	private int nbVote;
	private final Object lock = new Object();
	private String winner;
	private HashMap<String,Integer> map = new HashMap<>();

	public Vote(int nbMax) {
		if(nbMax < 0) {
			throw new IllegalArgumentException();
		}
		synchronized(lock) {
			nbVoteMax = nbMax;
		}	
	}

	private void updateMap(String str) {
		Objects.requireNonNull(str);
		synchronized(lock) {
			nbVote++;
			map.merge(str, 1, Integer::sum);	
		}
	}

	public String vote(String str) throws InterruptedException {
		synchronized(lock) {
			if(nbVoteMax == nbVote) {
				return winner;
			}
			updateMap(str);
			if(nbVote == nbVoteMax) {
				lock.notifyAll();
				winner = computeWinner();
			}
			while(nbVote != nbVoteMax) {
				lock.wait();
			}
			return winner;
		}
	}

	private String computeWinner() {
		synchronized(lock) {
			var score = -1;
			String winner = null;
			for (var e : map.entrySet()) {
				var key = e.getKey();
				var value = e.getValue();
				if (value > score || (value == score && key.compareTo(winner) < 0)) {
					winner = key;
					score = value;
				}
			}
			return winner;
		}
		
	}

	public static void main(String[] args) throws InterruptedException {
		var vote = new Vote(4);
		Thread.ofPlatform().start(() -> {
			try {
				Thread.sleep(2_000);
				System.out.println("The winner is " + vote.vote("un"));
			} catch (InterruptedException e) {
				throw new AssertionError(e);
			}
		});
		Thread.ofPlatform().start(() -> {
			try {
				Thread.sleep(1_500);
				System.out.println("The winner is " + vote.vote("zero"));
			} catch (InterruptedException e) {
				throw new AssertionError(e);
			}
		});
		Thread.ofPlatform().start(() -> {
			try {
				Thread.sleep(1_000);
				System.out.println("The winner is " + vote.vote("un"));
			} catch (InterruptedException e) {
				throw new AssertionError(e);
			}
		});
		System.out.println("The winner is " + vote.vote("zero"));
	}
}
