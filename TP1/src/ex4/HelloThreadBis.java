package ex4;

import ex1.HelloThread;

public class HelloThreadBis {

	public static void println(String s){
	  for(var i = 0; i < s.length(); i++){
	    System.out.print(s.charAt(i));
	  }
	  System.out.print("\n");
	}
	
	@SuppressWarnings("preview")
	public void newThread(int value) {
		Thread.ofPlatform().name("Hello " + value + " ").start(()->{
			for(var i=0;i < 5000;i++) {
				var str = Thread.currentThread().getName() + i;
				HelloThreadBis.println(str);
			}
		});
	}
	
	public static void main(String[] args) {
		var th = new HelloThread();
		for(var i=0;i<4;i++) {
			th.newThread(i);
		}
	}
}
