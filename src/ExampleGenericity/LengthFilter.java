package ExampleGenericity;

public class LengthFilter extends GenericFilter {

	@Override
	XLog filter(XLog log, GenericFilterConfiguration genericFilterConfiguration) {
		
		XLog resultingLog = new XLog(log);
		
		for(int i = 0; i < log.getTraces().length; i++) {
			
			if (log.getTraces()[i].getEvents().length > (int) genericFilterConfiguration.retrieveParameters()) {
				
				for (int j = 0; j < log.getTraces()[i].getEvents().length; j++) {
					resultingLog.getTraces()[i].getEvents()[j].setValue(0);
				}
				
				
			}
			
		}
		
		System.out.println(resultingLog);
		
		return resultingLog;
	}

	@Override
	XTrace filter(XTrace trace, GenericFilterConfiguration genericFilterConfiguration) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	XEvent filter(XEvent event, GenericFilterConfiguration genericFilterConfiguration) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	GenericFilterConfiguration createFilterConfiguration(XLog log) {
		// TODO Auto-generated method stub
		return null;
	}

}
