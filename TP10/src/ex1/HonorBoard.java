package ex1;

import java.util.Objects;

public class HonorBoard {
	record Person(String firstName,String lastName) {
		public String toString() {
			return firstName+" "+lastName;
		}
	}
	
	private volatile Person person = new Person("Mister","X");

	public void set(String firstName, String lastName) {
		Objects.requireNonNull(firstName);
		Objects.requireNonNull(lastName);
		this.person = new Person(firstName, lastName);
	}

	@Override
	public String toString() {
		return person.toString();
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