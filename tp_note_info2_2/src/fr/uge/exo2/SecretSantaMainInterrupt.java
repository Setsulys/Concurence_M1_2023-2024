package fr.uge.exo2;

import java.util.List;

public class SecretSantaMainInterrupt {
	public static void main(String[] args) throws InterruptedException {
		  var secretSanta = new SecretSantaInterrupt(5, List.of(0, 1, 3, 4, 2));
		  int[] waitingTimes = {500, 1_000, 1_500, 2_000, 2_500, 3_000};
		  var threads = new Thread[waitingTimes.length];
		  for (var id = 0; id < waitingTimes.length; id++) {
		      var waitingTime = waitingTimes[id];
		      threads[id]=Thread.ofPlatform().name("Thread " + id).start(() -> {
		          var currentThreadName = Thread.currentThread().getName();
		          try {
		              Thread.sleep(waitingTime);
		              System.out.println(currentThreadName + " received " + secretSanta.submit(currentThreadName));
		          } catch (InterruptedException e) {
		              System.out.println(currentThreadName + " has been stopped");
		          }
		      });
		  }
		  Thread.sleep(1_600);
		  threads[2].interrupt();
		}    
}