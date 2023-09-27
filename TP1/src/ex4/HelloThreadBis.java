package ex4;

public class HelloThreadBis {

  public static void println(String s) {
    for (var i = 0; i < s.length(); i++) {
      System.out.print(s.charAt(i));
    }
    System.out.print("\n");
  }

public static void main(String[] args) {
    int nbThreads = 5;

    for (int j = 0; j < nbThreads; j++) {
      int actuallyFinal = j;
      Thread.ofPlatform().start(() -> {
        for (int i = 0; i < 5000; i++) {
          println("hello " + actuallyFinal + " " + i);
        }
      });
    }
  }
}
