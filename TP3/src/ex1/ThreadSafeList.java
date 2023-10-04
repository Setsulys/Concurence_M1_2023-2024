package ex1;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class ThreadSafeList<E> {

	private final ArrayList<E> list = new ArrayList<>();
	private final Object lock  = new Object();
	
	public void add(E element) {
		Objects.requireNonNull(element);
		synchronized (lock) {
			list.add(element);
		}
	}
	
	public int size() {
		return list.size();
	}
	
	@Override
	public String toString() {
		return list.stream().map(String::valueOf).collect(Collectors.joining(","));
	} 
	
	
	
}
