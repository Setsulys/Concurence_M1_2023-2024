package ex1;

public class Exchanger<T> {

	private T value;
	private State state = State.WAITING;
	private final Object lock = new Object();

	enum State{
		BEGIN,
		WAITING
	}

	public T exchange(T newValue) throws InterruptedException {
		synchronized (lock) {
			if(state != State.BEGIN) {
				value = newValue;
				state = State.BEGIN;
				while(state ==State.BEGIN) {
					lock.wait();
				}
				return value;
			}
			else {
				var tmp = value;
				value = newValue;
				state = State.WAITING;
				lock.notify();
				return tmp;
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
		var exchanger = new Exchanger<String>();
		Thread.ofPlatform().start(() -> {
			try {
				System.out.println("thread 1 " + exchanger.exchange("foo1"));
			} catch (InterruptedException e) {
				throw new AssertionError(e);
			}
		});
		System.out.println("main " + exchanger.exchange(null));
	}
}
