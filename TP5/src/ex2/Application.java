package ex2;

import java.util.ArrayList;
import java.util.List;

public class Application {
	public static void main(String[] args) throws InterruptedException {
		var rooms = List.of("bedroom1", "bedroom2", "kitchen", "dining-room", "bathroom", "toilets");

		var temperatures = new ArrayList<Integer>();

		for (String room : rooms) {
			var temperature = Heat4J.retrieveTemperature(room);
			System.out.println("Temperature in room " + room + " : " + temperature);
			temperatures.add(temperature);
		}

		System.out.println(temperatures.stream().mapToInt(Integer::intValue).average().getAsDouble());
	}
}
