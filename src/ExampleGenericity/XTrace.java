package ExampleGenericity;
import java.util.Random;

public class XTrace {
	
	private XEvent[] events;
	
	public XTrace(int numberOfEvents) {
		events = new XEvent[numberOfEvents];
		
		Random random = new Random();
		
		for (int i = 0; i < numberOfEvents; i++) {
			events[i] = new XEvent(random.nextInt(10) + 1);
		}
		
	}
	
	public XEvent[] getEvents() {
		return events;
	}
	
	@Override
	public String toString() {
		String string = "";
		
		for (int i = 0; i < events.length; i++) {
			string += events[i];
			string += "\t";
		}
		
		string += "\n";
		
		return string;
	}

}
