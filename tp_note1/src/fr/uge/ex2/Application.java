package fr.uge.ex2;

public class Application {

	public static void main(String[] args) {
		var nbThread = 5;
		var bid = new Bid();
		for(var i= 0; i <nbThread ; i++ ) {
			var j = i+1;
			Thread.ofPlatform().name(j+"").start(()->{
				var k = j;
				try {
					var e = bid.propose(j);
					for(;;) {
						Thread.sleep(1_000);
						if(e.isEmpty()) { //vide donc pas retenu
							k++;
							e = bid.propose(k);
						}
						else { //le plus petit re propose une valeur superieur a la moyenne
							k= e.get();
							e = bid.propose(k);
							
						}
					}
				} catch (InterruptedException e) {
					throw new AssertionError(e);

				}
			});
		}
	}
}
