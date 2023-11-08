package ex1;

import java.util.OptionalLong;
import java.util.concurrent.ThreadLocalRandom;

public class Tests {

	public static boolean isPrime(long candidate) {
		if (candidate <= 1) {
			return false;
		}
		for (var i = 2; i <= Math.sqrt(candidate) && !Thread.currentThread().isInterrupted(); i++) {
			if (candidate % i == 0) {
				return false;
			}
		}
		if(Thread.currentThread().isInterrupted()) {
			return false;
		}
		return true;
	}

	public static OptionalLong findPrime() {
		var generator = ThreadLocalRandom.current();
		for (;!Thread.interrupted();) {
			var candidate = generator.nextLong();
			if (isPrime(candidate)) {
				return OptionalLong.of(candidate);
			}
		}
		return OptionalLong.empty();
	}

	public static void main(String[] args) throws InterruptedException {
		var thread = Thread.ofPlatform().start(() -> {
			System.out.println("Found a random prime : " + findPrime());
		});

		Thread.sleep(3_000);
		System.out.println("STOP");
		thread.interrupt();
	}
}
