package ex2;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Heat4JThreadSafe {

	private final Object lock = new Object();
	private final Map<String,Integer> temp;
	private final int roomNumber;
	
	public Heat4JThreadSafe(int numberOfRooms) {
		if(numberOfRooms <0) {
			throw new IllegalArgumentException();
		}
		synchronized(lock) {
			temp = new HashMap<>();
			roomNumber = numberOfRooms;	
		}

	}
	
	public int retrieveTemperature(String roomName) throws InterruptedException {
		synchronized (lock) {
			Thread.sleep(Math.abs(ThreadLocalRandom.current().nextInt() %1000));
			var roomTemp = 10 + (ThreadLocalRandom.current().nextInt() %20);
			temp.put(roomName,roomTemp);
			if(temp.size() == roomNumber) {
				lock.notify();
			}
			return roomTemp;
		}
	}
	
	public double getAverageTemperature() throws InterruptedException {
		synchronized (lock) {
			lock.wait();
			return temp.values().stream().mapToInt(Integer::intValue).average().getAsDouble();
		}
	}
}
