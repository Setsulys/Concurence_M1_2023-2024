package ex1;
import java.util.concurrent.Exchanger;

public class ExchangerExample {
  public static void main(String[] args) throws InterruptedException {
    var exchanger = new Exchanger<String>();
    Thread.ofPlatform().start(() -> {
      try {
        System.out.println("thread 1 " + exchanger.exchange("foo1"));
      } catch (InterruptedException e) {
        throw new AssertionError(e);
      }
    });
    System.out.println("main " + exchanger.exchange(null));
  }
}

/**
 * Quel est l'affichage attendu ?
 * l'affichage attendu sera
 * thread 1 null;
 * main foo1
 * 
 * Comment faire pour distinguer le premier et le second appel à la méthode exchange ?
 * on utilise un boolean ou un enum
 */
