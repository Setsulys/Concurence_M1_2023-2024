package ex2;

class ExampleLongAffectation {
	  long l = -1L;

	  public static void main(String[] args) {
	    var e = new ExampleLongAffectation();
	    Thread.ofPlatform().start(() -> {
	      System.out.println("l = " + e.l);
	    });
	    e.l = 0;
	  }
	}

/**2 Quand on exécute le code précédent, quels peuvent être les différents affichages constatés ?
 * l = 0
 * l = -1
 * Il est possible que le thread soit deschédulé entre les bits de poids fort et de poids faible dans le long d'où les deux affichage différents
 */
