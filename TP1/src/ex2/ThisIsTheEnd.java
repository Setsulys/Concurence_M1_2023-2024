package ex2;

import java.util.ArrayList;

public class ThisIsTheEnd {
	private final ArrayList<Thread> ths = new ArrayList();
	
	public void newThread(int value){
		var th = Thread.ofPlatform().name("Hello "+ value + " ").start(()->{
			for(var i=0;i < 5000;i++) {
				System.out.println(Thread.currentThread().getName()+ i);
			}
		});
		ths.add(th);
	}
	
	public static void main(String[] args){
		var th = new ThisIsTheEnd();
		for(var i=0;i<4;i++) {
			th.newThread(i);
		}
		th.ths.stream().forEach(e -> {
			try {
				e.join();
			} catch (InterruptedException e1) {
				return;
			}
		});;
		System.out.println("Le thread a fini son runnable");
	}
}
