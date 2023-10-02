package ex0;

public class Counter {
    private int value;

    public void addALot() {
        for (var i = 0; i < 100_000; i++) {
            this.value++;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        var counter = new Counter();
        var thread1 = Thread.ofPlatform().start(counter::addALot);
        var thread2 = Thread.ofPlatform().start(counter::addALot);
        thread1.join();
        thread2.join();
        System.out.println(counter.value);
    }
}
/**
 * 2 Essayez d'expliquer ce que vous observez.
 * A chaque execution de la classe, on obtient une valeur différente qui sera toujours superieur a 100_000
 * 
 * 3 Est-il possible que ce code affiche moins que 100 000 ? Expliquer précisément pourquoi.
 * Oui, c'est possible mais c'est très rare. En effet, il y a plusieurs threads qui s'incrémentent jusqu'a 100_000.
 * Cependant, la ligne ``this.value++`` n'est pas atomique, en bytecode, elle sera traduite en plusieurs lignes
 * Comme il y a plusieurs threads qui sont en écriture, il peut y avoir des effets de bords.
 * Il se peut que l'un des threads s'execute jusqu'a avant de l'ajouter puis se bloque
 * Le deuxieme thread se lance et s'execute entierement jusqu'a 9_999 et se stope mais le thread 1 reprend la main et donc ecrase les 9_999
 * puis de meme pour le thread 2 
 */

