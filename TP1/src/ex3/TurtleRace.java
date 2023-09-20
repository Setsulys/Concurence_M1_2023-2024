package ex3;

public class TurtleRace{

	public static void main(String[] args) throws InterruptedException {
		  System.out.println("On your mark!");
		  Thread.sleep(30_000);
		  System.out.println("Go!");
		  int[] times = {25_000, 10_000, 20_000, 5_000, 50_000, 60_000};
		  
		  for(var i =0;i < times.length;i++) {
			  var j = i;
			  Thread.ofPlatform().name("Turtle"+ j).start(()-> { 
				  try {
					Thread.sleep(times[j]);
					System.out.println(Thread.currentThread().getName() + " has finished");
				} catch (InterruptedException e) {
					return;
				}
			  });
		  }
		}
}

/**
* Observer l'évolution du nombre de threads. Que devient le thread main ? Quand est-ce que la JVM s'éteint ?
* Le thread main s'arrete.
*/