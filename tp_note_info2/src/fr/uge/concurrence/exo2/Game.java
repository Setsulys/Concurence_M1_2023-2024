package fr.uge.concurrence.exo2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class Game {
	private final Object lock = new Object();
	private final HashMap<Thread,Move> th = new HashMap<>();
	
    public enum Result {WIN, LOSE, DRAW}

    public enum Move {ROCK, PAPER, SCISSOR;
        public Result against(Move other){
            if (this == other){
                return Result.DRAW;
            }
            if ((this == ROCK && other == SCISSOR) || 
                (this == PAPER && other == ROCK) || 
                (this == SCISSOR && other == PAPER)){
                return Result.WIN;
            }
            return Result.LOSE;
        }

        public static Move random(){
             return Move.values()[ThreadLocalRandom.current().nextInt(Move.values().length)];
        }
    }
	
    public void setValue(Move move) {
    	synchronized(lock) {
    		th.put(Thread.currentThread(), move);
    	}
    }
    
	public Result results() throws InterruptedException {
		synchronized(lock) {
			while(th.size()!=2) {
				lock.wait();
			}
			lock.notifyAll();
			var other = new ArrayList<>(th.keySet());
			other.remove(Thread.currentThread());
			return th.get(Thread.currentThread()).against(th.get(other.get(0)));
		}
	}
}
