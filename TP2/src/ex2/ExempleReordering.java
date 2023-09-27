package ex2;

class ExempleReordering {
	  int a = 0;
	  int b = 0;

	  public static void main(String[] args) {
	    var e = 	new ExempleReordering();
	    Thread.ofPlatform().start(() -> {
	      System.out.println("a = " + e.a + "  b = " + e.b);
	    });
	    e.a = 1;
	    e.b = 2;
	  }
	}

/* 1 Quand on exécute le code précédent, quels peuvent être les différents affichages constatés ?	
 * a= 0 b =0 tout est executé dans l'ordre
 * a=1 b=2 si le schéduler execute tout le main
 * a=0 b=2 ce cas est provoqué par le Jit qui reordonne les lignes
 */