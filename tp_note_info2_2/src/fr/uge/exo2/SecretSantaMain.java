package fr.uge.exo2;

import java.util.List;

public class SecretSantaMain {
	public static void main(String[] args) throws InterruptedException {
        var secretSanta = new SecretSanta(5, List.of(0, 1, 3, 4, 2));
        int[] waitingTimes = {500, 1000, 1500, 2000, 2500};
        for (var id = 0; id < waitingTimes.length; id++) {
            var waitingTime = waitingTimes[id];
            Thread.ofPlatform().name("Thread " + id).start(() -> {
                try {
                    Thread.sleep(waitingTime);
                    var currentThreadName = Thread.currentThread().getName();
                    System.out.println(currentThreadName + " received " + secretSanta.submit(currentThreadName));
                } catch (InterruptedException e) {
                    throw new AssertionError(e);
                }
            });
        }
		int[] observedSizes = {1,2,3,4,5};
		var observers = new Thread[observedSizes.length];
		for (var id = 0; id < observedSizes.length; id++) {
		    var observedSize = observedSizes[id];
		    observers[id]=Thread.ofPlatform().name("Observer " + id).start(() -> {
		        var observerName = Thread.currentThread().getName();
		        try {
		            System.out.println(observerName + " observed " + secretSanta.observe(observedSize));
		        } catch (InterruptedException e) {
		            System.out.println(observerName + " did not observe any thing as a InterruptedException was thrown");
		        }
		    });
		}
		Thread.sleep(1100);
		observers[2].interrupt(); 
    } 
	
	
}
