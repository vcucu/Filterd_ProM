package timeframe;

import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JComponent;

import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.filterd.configurations.FilterdAbstractConfig;
import org.processmining.filterd.filters.Filter;

public class TimeframeParameters extends FilterdAbstractConfig {
	
	protected Filter filter;
	protected String name;
	protected String key;
	private boolean removeEmptyTraces;
	private XLog log;
	private UIPluginContext context;
	private String lowerTime;
	private String upperTime;

	private ArrayList<String> times; 
	
	public TimeframeParameters() {
		name = "";
		removeEmptyTraces = false;
		filter = new TimeframeAlgorithm();
		times = new ArrayList<>();
		lowerTime = "";
		upperTime = "";
	}
	
	public TimeframeParameters(UIPluginContext context, XLog log){
		this();
		this.log = log;
		this.context = context;
		
		for (XTrace trace: log) {
			for (XEvent event : trace) {
				for (String key : event.getAttributes().keySet()) {
					if (key.contains("time:timestamp")) {
							/* timestamp format YYYY-MM-DDTHH:MM:SS.ssssGMT with GMT = {Z, + , -} */
							String time = event.getAttributes().get(key).toString();
							/* get only yyy-mm-ddThh:mm:ss */
							if (time.contains(".")) {
								times.add(time.split("\\.")[0]);
							} else {
								if (time.contains("+")) {
									times.add(time.split("\\+")[0]);
								} else if (time.contains("Z")) {
									times.add(time.split("Z")[0]);
								} else if (time.contains("-")) {
									times.add(time.split("\\-")[0]);
								}
							}
					}
				}
			}
		}

		/* sort the timestamps in ascending order */
		Collections.sort(times);
		name = XConceptExtension.instance().extractName(log);
	}

	public boolean equals(Object object) {
		if(object instanceof TimeframeParameters) {
			TimeframeParameters timeframeParameters = (TimeframeParameters) object;
			String lowerTimePar = timeframeParameters.lowerTime;
			String upperTimePar = timeframeParameters.upperTime;
			
			return (this.lowerTime.compareTo(lowerTimePar) == 0) && 
					(this.upperTime.compareTo(upperTimePar) == 0) && 
					this.getName() == timeframeParameters.getName();
		}
		
		return false;
	}

	
	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	public FilterdParameters apply(JComponent component) {
		TimeframePanel panel = (TimeframePanel) component;
		
		this.setName(panel.getNameLabel().getText());
		this.setRemoveEmptyTraces(panel.getRemoveEmptyTracesComponent().isSelected());
		
		
		return null;
	}

	private void setRemoveEmptyTraces(boolean remove) {
		this.removeEmptyTraces = remove;
		
	}

	public void setName(String name) {
		this.name = name;
		
	}
	
	public String getName() {
		return name;
	}

	public void setUpper(String upperTime) {
		this.upperTime = upperTime;
		
	}

	public void setLower(String lowerTime) {
		this.lowerTime = lowerTime;
		
	}
	
	public String getLowerTime() {
		return lowerTime;
	}

	public String getUpperTime() {
		return upperTime;
	}

	public boolean canApply(JComponent component) {
		return (component instanceof TimeframePanel);
	}

	public JComponent getPropertiesPanel() {
		return new TimeframePanel(context, this);
	}

	public boolean isRemoveEmptyTraces() {
		return removeEmptyTraces;
	}
	
	public ArrayList<String> getTimes() {
		return times;
	}
	
	public Filter getFilter() {
		return this.filter;
	}

}
