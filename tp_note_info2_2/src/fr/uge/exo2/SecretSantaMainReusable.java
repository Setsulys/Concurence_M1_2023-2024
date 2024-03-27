package fr.uge.exo2;

import java.util.List;

public class SecretSantaMainReusable {
	public static void main(String[] args) throws InterruptedException {
		var nbParticipants = 5;
		var secretSantaReusable = new SecretSantaReusable(nbParticipants, List.of(1, 2, 3, 4, 0));
		for (var id = 0; id < nbParticipants * 3; id++) {
		    Thread.ofPlatform().name("Thread " + id).start(() -> {
		        try {
		            var currentThreadName = Thread.currentThread().getName();
		            System.out.println(currentThreadName + " received " + secretSantaReusable.submit(currentThreadName));
		        } catch (InterruptedException e) {
		            throw new AssertionError(e);
		        }
		    });
		}
    } 
	
	
}
