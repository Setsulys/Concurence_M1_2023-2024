package ex3;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class HelloListBug {

	public static void main(String[] args) throws InterruptedException {
		  var nbThreads = 4;
		  var threads = new Thread[nbThreads];
		  var list = new ArrayList<Integer>(5000 * nbThreads);

		  IntStream.range(0, nbThreads).forEach(j -> {
		    Runnable runnable = () -> {
		      for (var i = 0; i < 5000; i++) {
		        //System.out.println("hello " + j + " " + i);
		        list.add(i);
		      }
		    };

		    threads[j] = Thread.ofPlatform().start(runnable);
		  });

		  for (var thread : threads) {
		    thread.join(); 
		  }
		  System.out.println(list.size());
		  System.out.println("le programme est fini");
		}
}

/**
 *  2 Exécuter le programme plusieurs fois et noter les différents affichages
 *  Cela affiche une valeur différente a chaque execution (pour ma part entre 6000 et 8000)
 *  
 *  3 Expliquer comment la taille de la liste peut être plus petite que le nombre total d'appels à la méthode add.
 *  Il est possible que dans le cache, il ai eu des problème d'acces à la mémoire, comme on est en lecture écriture sur le tas, il y a des effets de bords de cette non atmicité
 */
