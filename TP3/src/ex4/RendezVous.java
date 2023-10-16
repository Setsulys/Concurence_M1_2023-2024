package ex4;

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
    }
  }
  
  public V get() throws InterruptedException {
	
		while(value == null) {
			synchronized(lock) {
        	//Thread.sleep(1);  // then comment this line !
			}
    	}
		return value;
	
  }
  
  public static void main(String[] args) throws InterruptedException {
    RendezVous<String> rendezVous = new RendezVous<>();
    Thread.ofPlatform().start(() -> {
      try {
        Thread.sleep(5000);
      } catch (InterruptedException e) {
        throw new AssertionError(e);
      }
      rendezVous.set("hello");
    });
    
    System.out.println(rendezVous.get());
  }
}
