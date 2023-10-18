package ex2;

import java.util.List;

public class ApplicationBis {
	public static void main(String[] args) throws InterruptedException {
		var rooms = List.of("bedroom1", "bedroom2", "kitchen", "dining-room", "bathroom", "toilets");
		var h4j = new Heat4JThreadSafe(rooms.size());
		for(var i=0;i<rooms.size();i++) {
			var j =i;
			Thread.ofPlatform().start(() ->{
				for(;;) {
					try {

						System.out.println("Temperature in room " + rooms.get(j) + " "+ h4j.retrieveTemperature(rooms.get(j)));
					} catch (InterruptedException e) {
						throw new AssertionError(e);
					}
				}
			});
		}
		for(;;) {
			System.out.println(h4j.getAverageTemperature());
		}
		
	}
}
