package fr.uge.concurrence.exo2;

public class MainGame {
	public static void main(String[] args) {
		var game = new Game();
		Thread.ofPlatform().name("A").start(()->{
			var choice =Game.Move.random();
			System.out.println(Thread.currentThread().getName()+" plays " + choice);
			game.setValue(choice);
			try {
				System.out.println("The result for "+Thread.currentThread().getName() +" is "+ game.results());
			} catch (InterruptedException e) {
				throw new AssertionError(e);
			}
		});
		Thread.ofPlatform().name("B").start(()->{
			var choice =Game.Move.random();
			System.out.println(Thread.currentThread().getName()+" plays " + choice);
			game.setValue(choice);
			try {
				System.out.println("The result for "+Thread.currentThread().getName() +" is "+ game.results());
			} catch (InterruptedException e) {
				throw new AssertionError(e);
			}
		});

	}
}
