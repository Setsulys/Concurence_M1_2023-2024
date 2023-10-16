package ex1;

import java.util.Objects;

/**
 * Note: this code does several stupid things !
 */
public class RendezVous<V> {
  private V value;
  private final Object lock = new Object();
  
  public void set(V value) {
    Objects.requireNonNull(value);
    synchronized(lock) {
    	this.value = value;
    	lock.notify();
    }
  }
  
  public V get() throws InterruptedException {
	synchronized(lock) {
		while(value == null) {
        	//Thread.sleep(1);  // then comment this line !
			lock.wait();
    	}
		return value;
	}
	
  }
  
  public static void main(String[] args) throws InterruptedException {
	    var rdv = new RendezVous<String>();
	    Thread.ofPlatform().start(() -> {
	      try {
	        Thread.sleep(20_000);
	        rdv.set("Message");
	      } catch (InterruptedException e) {
	        throw new AssertionError(e);
	      }
	    });
	    System.out.println(rdv.get());
	  }
  
  
}
