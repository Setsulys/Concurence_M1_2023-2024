package fr.uge.exam.exo1;

public class SackMain {

	public static void main(String[] args) {
		var sack = new Sack(500);
		Thread.ofPlatform().start(()->{
			for(;;) {
				try {
					Thread.sleep(100);
					sack.putGift(10);
					System.out.println("<----10");
					sack.putGift(5);
					System.out.println("<-----5");
				} catch (InterruptedException e) {
					throw new AssertionError(e);
				}
			}

		});
		Thread.ofPlatform().start(()->{
			for(;;) {
				try {
					Thread.sleep(200);
					System.out.println("REMOVE----->" +sack.takeGift());
				} catch (InterruptedException e) {
					throw new AssertionError(e);
				}
			}
		});
		for(var i =0; i< 2;i++) {
			Thread.ofPlatform().start(()->{
				for(;;) {
					try {
						Thread.sleep(1000);
						System.out.println("REMOVE()--->"+sack.takeGiftsUntil(1000).stream().mapToInt(e->e).sum());
					}catch(InterruptedException e) {
						throw new AssertionError(e);
					}
				}
			});
		}
		Thread.ofPlatform().start(()->{
			for(;;) {
				try {
					Thread.sleep(100);
					System.out.println("Weight Needed " + sack.weightNeeded());
				}catch(InterruptedException e) {
					throw new AssertionError(e); 
				}
			}
		});
	}
}
