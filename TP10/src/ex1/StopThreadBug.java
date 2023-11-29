package ex1;

public class StopThreadBug {
	  private volatile boolean stop;

	  public void runCounter() {
	    var localCounter = 0;
	    for(;;) {
	      if (stop) {
	        break;
	      }
	      localCounter++;
	    }
	    System.out.println(localCounter);
	  }

	  public void stop() {
	    stop = true;
	  }

	  public static void main(String[] args) throws InterruptedException {
	    var bogus = new StopThreadBug();
	    var thread = Thread.ofPlatform().start(bogus::runCounter);
	    Thread.sleep(100);
	    bogus.stop();
	    thread.join();
	  }
	}