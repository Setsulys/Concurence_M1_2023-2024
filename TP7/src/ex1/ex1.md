# Haut les mains

### 1. Pourquoi n'est il pas possible d’arrêter un thread de façon non coopérative ?

Il n'est pas possible d'arrêter un thread de façon



### 2. Rappeler ce qu'est une opération bloquante.

Une opération bloquante va bloquer le thread en attendant que l'opération soit valide pour débloquer le thread

### 3. À quoi sert la méthode d'instance interrupt() de la classe Thread?

La méthode d'instance ``interrupt()`` de la classe Thread sert à verifier si un thread a été intérrompu.

### 4. Expliquer comment interrompre un thread en train d'effectuer une opération bloquante et le faire sur l'exemple suivant : le thread main attend 5 secondes avant d'interrompre le thread qui dort et ce dernier affiche son nom.

```java
  public static void main(String[] args) {
    var thread = Thread.ofPlatform().start(() -> {
      for (var i = 1;; i++) {
        try {
          Thread.sleep(1_000);
          System.out.println("Thread slept " + i + " seconds.");
        } catch (InterruptedException e) {
        	System.out.println(Thread.currentThread());
          return;
        }
      }
    });
    Thread.sleep(5_000);
    thread.interrupt();
  }
```

### 5. Expliquer, sur l'exemple suivant, comment utiliser la méthode Thread.interrupted pour arrêter le calcul de findPrime() qui n'est pas une opération bloquante. Modifier le code de findPrime (mais ni sa signature, ni isPrime) pour pouvoir l'interrompre. Dans ce cas, elle renvoie un OptionalLong vide.<br>Puis faire en sorte que le main attende 3 secondes avant d'interrompre le thread qui cherche un nombre premier, en affichant "STOP".

```java
  public static boolean isPrime(long candidate) {
    if (candidate <= 1) {
      return false;
    }
    for (var i = 2; i <= Math.sqrt(candidate); i++) {
      if (candidate % i == 0) {
        return false;
      }
    }
    return true;
  }

  public static OptionalLong findPrime() {
    var generator = ThreadLocalRandom.current();
    for (;Thread.interrupted();) {
      var candidate = generator.nextLong();
      if (isPrime(candidate)) {
        return OptionalLong.of(candidate);
      }
    }
    return Optionallong.empty();
  }

  public static void main(String[] args) throws InterruptedException {
    var thread = Thread.ofPlatform().start(() -> {
      System.out.println("Found a random prime : " + findPrime().orElseThrow());
    });
    
    Thread.sleep(3_000);
    System.out.println("STOP");
    thread.interrupt();
  }
```

### 6. Expliquer la (trop) subtile différence entre les méthodes Thread.interrupted et thread.isInterrupted de la classe Thread.

La "trop" subtile différence entre les méthodes ``Thread.interrupted()`` et ``Thread.isInterrupted()`` c'est le fait que le flag de verification d'interruption se réinitialise sur le statuts sur false pour ```Thread.interrupted()``



### 7. On souhaite maintenant faire en sorte que findPrime s'arrête dès que possible si le thread qui l’utilise est interrompu. Pour cela, modifier le code de findPrime et/ou isPrime sans modifier leur signature.

```java
	public static boolean isPrime(long candidate) {
		if (candidate <= 1) {
			return false;
		}
		for (var i = 2; i <= Math.sqrt(candidate) && !Thread.interrupted(); i++) {
			if (candidate % i == 0) {
				return false;
			}
		}
		return true;
	}

	public static OptionalLong findPrime() {
		var generator = ThreadLocalRandom.current();
		for (;!Thread.interrupted();) {
			var candidate = generator.nextLong();
			if (isPrime(candidate)) {
				return OptionalLong.of(candidate);
			}
		}
		return OptionalLong.empty();
	}

	public static void main(String[] args) throws InterruptedException {
		var thread = Thread.ofPlatform().start(() -> {
			System.out.println("Found a random prime : " + findPrime());
		});

		Thread.sleep(3_000);
		System.out.println("STOP");
		thread.interrupt();
	}
```

### 8. Et si vous pouvez modifier le code des méthodes ET leur signature, que faites-vous



### 9. Pouvez-vous garantir que le programme afichera soit un nombre, soit "STOP", mais pas les deux ?

Non, il est possible dans de rare cas qu'on ai un stop mais aussi un nombre en meme temps



