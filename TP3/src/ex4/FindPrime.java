package ex4;

import java.util.Random;

public class FindPrime {
  public static boolean isPrime(long l) {
    if (l <= 1)
      return false;
    for (long i = 2L; i <= l / 2; i++) {
      if (l % i == 0) {
        return false;
      }
    }
    return true;
  }

  public static void main(String[] args) throws InterruptedException {
    var nbThreads = 4;
    var rdv = new RendezVous<Long>();
    for (var i = 0; i < nbThreads; i++) {
      var fi = i;
      Thread.ofPlatform().daemon().start(() -> {
        var random = new Random();
        for (;;) {
          var nb = 10_000_000_000L + (random.nextLong() % 10_000_000_000L);
          if (isPrime(nb)) {
            rdv.set(nb);
            System.out.println("A prime number was found in thread " + fi);
            return;
          }
        }
      });
    }
    Long prime = rdv.get();
    System.out.println("I found a large prime number : " + prime);
  }
}
/**
 * Que se passe-t-il lorsqu'on exécute ce code ?
 * Lorsque l'on execute ce code, après une courte attente, il nous est affiché un très grand nombre
 * 
 * Commenter l'instruction Thread.sleep(1) dans la méthode get puis ré-exécuter le code. Que se passe-t-il ? Expliquer où est le bug ?
 * Nous n'obtenons plus une valeurs mais des indications disant que le thread a trouvé un nombre premier élevé
 */