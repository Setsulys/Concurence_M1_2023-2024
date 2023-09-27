package ex1;


public class HelloThread {

	public void newThread(int value) {
		Thread.ofPlatform().start(()->{
			for(var i=0;i < 5000;i++) {
				System.out.println("Hello "+ value + " "+ i);
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


/**
 * 1 Rappeler à quoi sert un Runnable. 
 * Dans notre cas, le runnable sert a lancer le thread
 * 
 * 2 Exécutez le programme plusieurs fois, que remarque-t-on ? Puis, en regardant l'affichage (scroller au besoin), qu'y a-t-il de bizarre ? Est-ce que tout ceci est bien normal ?
 *  On remarque que pour chaque execution, nous n'obtenons pas le même résultat. C'est normal car les threads s'entre-mêlent.
 */
