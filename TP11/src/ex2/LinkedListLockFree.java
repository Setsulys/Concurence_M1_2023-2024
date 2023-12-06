package ex2;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class LinkedListLockFree<E> {
	private static final class Entry<E> {
		private final E element;
		private Entry<E> next;

		Entry(E element) {
			this.element = element;
		}
	}

	private final Entry<E> head = new Entry<>(null); // fake first entry

	private static final VarHandle TAIL_HANDLER;

	static {
		var lookup = MethodHandles.lookup();
		try{
			TAIL_HANDLER = lookup.findVarHandle(LinkedListLockFree.Entry.class, "next", Entry.class);
		}catch(NoSuchFieldException | IllegalAccessException e) {
			throw new AssertionError(e);
		}
	}


	public void addLast(E element) {
		var entry = new Entry<>(element);
		var current =head;
		for (;;) {
			if(TAIL_HANDLER.compareAndSet(current,null,entry)) {
				return;
			}
			var next = current.next;
			current = next;
		}
	}

	public int size() {
		var count = 0;
		for (var entry = (Entry<E>)TAIL_HANDLER.get(head); entry != null; entry = entry.next) {
			count++;
		}
		return count;
	}

	private static Runnable createRunnable(LinkedListLockFree<String> list, int id) {
		return () -> {
			for (var i = 0; i < 10_000; i++) {
				list.addLast(id + " " + i);
			}
		};
	}

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		var threadCount = 5;
		var list = new LinkedListLockFree<String>();
		var tasks = IntStream.range(0, threadCount).mapToObj(id -> createRunnable(list, id)).map(Executors::callable)
				.toList();
		var executor = Executors.newFixedThreadPool(threadCount);
		var futures = executor.invokeAll(tasks);
		executor.shutdown();
		for (var future : futures) {
			future.get();
		}
		System.out.println(list.size());
	}
}