package ex1;

public class Exchanger<T> {

	private T value;
	private State state = State.READY;
	private final Object lock = new Object();

	enum State{
		THEN,
		READY,
		FINISHED
	}

	public T exchange(T newValue) throws InterruptedException {
		synchronized (lock) {
			if(state != State.THEN) { //State is State.READY
				value = newValue;
				state = State.THEN;
				while(state ==State.THEN) {
					lock.wait();
				}
				state = State.READY;
				return value;
			}
			else {
				var tmp = value;
				value = newValue;
				state = State.FINISHED;
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
