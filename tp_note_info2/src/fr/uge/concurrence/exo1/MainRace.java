package fr.uge.concurrence.exo1;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class MainRace {
	public static void main(String[] args) {
		int nbThread =5;
		int nbParticipants = 4;
		int nbSteps = 3;
		var threads = new ArrayList<Thread>();
		var tR = new ThreadRace(nbParticipants,nbSteps);
		for(var i = 0 ; i < nbThread;i++) {
			threads.add(Thread.ofPlatform().start(()->{
				try {
					if(tR.enlist()) {
						System.out.println(Thread.currentThread().getName() +" prêt !");
					}
					for(var k=0;k < nbSteps;k++) {
						var arrives = tR.step();
						var affiche = "";
						if(arrives !=-1) {
							if(arrives > 0 ) {
								affiche+=" ("+arrives+" arrivés)";
							}
							System.out.println("-".repeat(k+1) +" "+Thread.currentThread().getName() +affiche );
							ThreadLocalRandom.current().nextInt(1000);
						}
					}
				} catch (InterruptedException e) {
					throw new AssertionError(e);
				}
			}));
		}
		for(var th : threads) {
			try {
				th.join();
			} catch (InterruptedException e) {
				throw new AssertionError(e);
			}
		}
		try {
			System.out.println("Resultat : " + tR.finish());
		} catch (InterruptedException e) {
			throw new AssertionError(e);
		}
	}
}
