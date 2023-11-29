### 1. Quels sont les affichages possibles du code ci-dessous 

```java
public class Duck {
    private volatile long a = 1;
    private volatile long b = 1;

    public void foo() {
        a = 2;
        b = -1;
    }

    public static void main(String[] args) {
        var duck = new Duck();
        Thread.ofPlatform().start(() -> {
            System.out.println("b = " + duck.b);
            System.out.println("a = " + duck.a);
        });
        Thread.ofPlatform().start(duck::foo);
    }
} 
```

```
b = 1
a = 1

b = -1
a = 2
```


### Le code ci-dessous, adapté du code de l'exercice 2 du TD 2, est problématique :
```java
public class StopThreadBug {
  private boolean stop;

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
```
### 2. Rappelez rapidement où est la data-race et pourquoi on peut observer que le programme ne s'arrête jamais.
La data race se fait sur stop, ca ne s'arrete jamais si stop ne deviens jamais true

### 3. Rendez la classe thread-safe sans utiliser de mécanisme de verrou. Quelle propriété garantit que le programme s'arrête ?
 - voir StopThreadBug
La propriété qui garantie que le programme s'arrête est que le volatile force l'ecriture et la lecture

### Reprenons la classe HonorBoard de l'exercice 2 de la séance 3. Cette fois-ci, les champs de la classe sont volatile.
```java
public class HonorBoard {
  private volatile String firstName;
  private volatile String lastName;
  
  public void set(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }
  
  @Override
  public String toString() {
    return firstName + ' ' + lastName;
  }
  
  public static void main(String[] args) {
    var board = new HonorBoard();
    Thread.ofPlatform().start(() -> {
      for(;;) {
        board.set("Mickey", "Mouse");
      }
    });
    
    Thread.ofPlatform().start(() -> {
      for(;;) {
        board.set("Donald", "Duck");
      }
    });
    
    Thread.ofPlatform().start(() -> {
      for(;;) {
        System.out.println(board);
      }
    });
  }
}
```

### 4. Est-il toujours possible de voir des affichages de Mickey Duck ou Donald Mouse ?
oui car fistName ne garantie pas lastName et inversement
### 5. Rendre la classe thread-safe en utilisant un seul champ volatile.
voir HonorBoard
