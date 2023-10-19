package ex2;

import java.util.HashMap;
import java.util.Map;

public class Heat4JThreadSafeBetter {

	private final Object lock = new Object();
	private final Map<String,Integer> temp;
	private final int roomNumber;
	private State state;
	
	enum State{
		FIRST,
		WAIT,
		DONE
	}
	
	public Heat4JThreadSafeBetter(int numberOfRooms) {
		if(numberOfRooms <0) {
			throw new IllegalArgumentException();
		}
		synchronized(lock) {
			temp = new HashMap<>();
			roomNumber = numberOfRooms;
			state = State.FIRST;
		}

	}
	
	public int retrieveTemperature(String roomName) throws InterruptedException {
		var roomTemp = Heat4J.retrieveTemperature(roomName);
		synchronized (lock) {
			temp.put(roomName,roomTemp);
			if(temp.size() == roomNumber) {
				lock.notify();
			}
			return roomTemp;
		}
	}
	
	public double getAverageTemperature() throws InterruptedException {
		synchronized (lock) {
			while(roomNumber!= temp.size()) {
				lock.wait();
			}
			return temp.values().stream().mapToInt(Integer::intValue).average().getAsDouble();
		}
	}
}
