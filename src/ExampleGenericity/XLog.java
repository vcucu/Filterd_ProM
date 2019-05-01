package ExampleGenericity;
import java.util.Random;

public class XLog {

	private XTrace traces[];
	
	public XLog(XLog log) {
		traces = new XTrace[10];
		
		for (int i = 0; i < 10; i++) {
			
			XTrace toCopyTrace = log.getTraces()[i];
			
			traces[i] = new XTrace(toCopyTrace.getEvents().length);
			
			for (int j = 0; j < toCopyTrace.getEvents().length; j++) {
				
				traces[i].getEvents()[j] = toCopyTrace.getEvents()[j];
				
			}
			
		}
		
	}
	
	public XLog() {
		traces = new XTrace[10];
		
		Random random = new Random();
		
		for (int i = 0; i < 10; i++) {
			traces[i] = new XTrace(random.nextInt(10) + 1);
		}
	}
	
	public XTrace[] getTraces() {
		return traces;
	}
	
	@Override
	public String toString() {
		String string = "---------------------------\n";
		
		for (int i = 0; i < traces.length; i++) {
			string += traces[i];
		}
		
		string += "---------------------------\n";
		
		return string;
	}
	
	
}
