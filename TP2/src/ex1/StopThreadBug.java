package ex1;

public class StopThreadBug implements Runnable {
    private boolean stop = false;

    public void stop() {
        stop = true;
    }

    @Override
    public void run() {
        while (!stop) {
            //System.out.println("Up");
        }
        System.out.print("Done");
    }

    public static void main(String[] args) throws InterruptedException {
        var stopThreadBug = new StopThreadBug();
        Thread.ofPlatform().start(stopThreadBug::run);
        Thread.sleep(5_000);
        System.out.println("Trying to tell the thread to stop");
        stopThreadBug.stop();
    }
}

/**
 * 1 Avant de l'exécuter, essayer de comprendre quel est le comportement espéré. Où se trouve la data-race ?
 * On veut lors de l'execution que cela affiche des "Up" puis on essaye de l'arreter. La data race se trouve dans le while
 * 
 * 2 Exécuter la classe plusieurs fois. Qu'observez-vous ?
 * Il n'y a pas de changement entre les executions.
 * 
 * 3 Modifiez la classe StopThreadBug.java pour supprimer l'affichage dans la boucle du thread. Exécuter la classe à nouveau plusieurs fois. Essayez d'expliquer ce comportement.
 * Lorsque l'on commente la ligne (ligne 13) le programme ne s'arrete pas car l'optimisation de JIT ne passe pas dans la boucle while et fini pas le programme
 * 
 * 4 Le code avec l'affichage va-t-il toujours finir par arrêter le thread ?
 * System.Out.Println() rechargera la RAM, donc on reverifiera la valeur de stop. Ce qui fera arreter le thread.
 */
