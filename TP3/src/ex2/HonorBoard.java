package ex2;

public class HonorBoard {
	private String firstName;
	private String lastName;
	private final Object lock = new Object();

	public void set(String firstName, String lastName) {
		synchronized (lock) {
			this.firstName = firstName;
			this.lastName = lastName;
		}
	}
	
	public String firstName() {
		return firstName;
	}
	public String lastName() {
		return lastName;
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
				System.out.println(board.firstName() + ' ' + board.lastName());
			}
		});
	}
}

/**
 * 1.  Expliquer pourquoi la classe HonorBoard n'est pas thread-safe.
 * La classe HonorBoard n'est pas threadSafe car on accede à la zone mémoire sans faire de synchronized lorsque l'on fait le set
 *
 */