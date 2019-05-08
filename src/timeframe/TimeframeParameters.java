package timeframe;

import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JComponent;

import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.filterd.algorithms.Filter;
import org.processmining.filterd.parameters.FilterdParameters;

public class TimeframeParameters extends FilterdParameters {
	
	protected Filter filter;
	protected String name;
	protected String key;
	private boolean removeEmptyTraces;
	private XLog log;
	private UIPluginContext context;
	private String lowerTime;
	private String upperTime;

	private ArrayList<XAttribute> times; 
	
	public TimeframeParameters() {
		name = "";
		removeEmptyTraces = false;
		filter = new TimeframeAlgorithm();
		times = new ArrayList<>();
		lowerTime = new String();
		upperTime = new String();
	}
	
	public TimeframeParameters(UIPluginContext context, XLog log){
		this();
		this.log = log;
		this.context = context;
		
		for (XTrace trace: log) {
			for (XEvent event : trace) {
				for (String key : event.getAttributes().keySet()) {
					if (key.contains("time:timestamp")) {
							times.add(event.getAttributes().get(key));
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
		this.upperTime = new String(upperTime);
		
	}

	public void setLower(String lowerTime) {
		this.lowerTime = new String(lowerTime);
		
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
	
	public ArrayList<XAttribute> getTimes() {
		return times;
	}
	
	public Filter getFilter() {
		return this.filter;
	}

}
