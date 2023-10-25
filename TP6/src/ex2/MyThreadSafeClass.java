package ex2;

import java.util.ArrayDeque;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MyThreadSafeClass {
	private static final int SIZE =10;
	private final ReentrantLock lock = new ReentrantLock();
	private final Condition condition = lock.newCondition();
	private final ArrayDeque<Long> list = new ArrayDeque<>(SIZE);
	
	public void put(long value) {
		lock.lock();
		try {
			if(list.size()==SIZE) {
				return;
			}
			list.add(value);
			condition.signal();
		}finally {
			lock.unlock();
		}
	}
	
	public ArrayDeque<Long> display() throws InterruptedException {
		lock.lock();
		try {
			while(list.size()< SIZE) {
				condition.await();
			}
			return list;
		}finally {
			lock.unlock();
		}
	}
}
