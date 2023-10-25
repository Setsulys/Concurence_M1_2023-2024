package ex2;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class SumFirstTenPrime {
	
	public static boolean isPrime(long l) {
	    if (l <= 1) {
	        return false;
	    }
	    for (long i = 2L; i <= l / 2; i++) {
	        if (l % i == 0) {
	            return false;
	        }
	    }
	    return true;
	}
	
	public static void main(String[] args) throws InterruptedException {
		var prime = new MyThreadSafeClass();
		var nbThread = 5;
		for(var i=0;i< nbThread;i++) {
			Thread.ofPlatform().start(()->{
				for(;;) {
					long nb = 1_000_000_000L + ThreadLocalRandom.current().nextLong(1_000_000_000L);
					if(isPrime(nb)) {
						prime.put(nb);
					}
				}
			});
		}
		System.out.println(prime.display().stream().map(String::valueOf).collect(Collectors.joining("\n","---\n","\n---")));
		System.out.println("The first ten primes sum is : "+prime.display().stream().mapToLong(Long::valueOf).sum());
	}
}
