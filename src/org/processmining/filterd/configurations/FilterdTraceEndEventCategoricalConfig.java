package org.processmining.filterd.configurations;
import java.util.ArrayList;
import java.util.List;

import org.deckfour.xes.classification.XEventAttributeClassifier;
import org.deckfour.xes.classification.XEventClasses;
import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.factory.XFactoryRegistry;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.gui.FilterConfigPanelController;
import org.processmining.filterd.gui.NestedFilterConfigPanelController;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterValueFromRange;
import org.processmining.filterd.parameters.ParameterYesNo;
import org.processmining.filterd.tools.Toolbox;
import org.processmining.filterd.widgets.ParameterController;
import org.processmining.filterd.widgets.ParameterMultipleFromSetController;
import org.processmining.filterd.widgets.ParameterValueFromRangeController;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Slider;

public class FilterdTraceEndEventCategoricalConfig extends FilterdAbstractReferenceableConfig {	
	
	XEventClasses xEventClasses;
	List<String> allValues = new ArrayList<>();
	
	String attribute;
	
	public FilterdTraceEndEventCategoricalConfig(XLog log, Filter filterType, String attribute, 
			List<XEventClassifier> classifiers) {
		super(log, filterType);
		parameters = new ArrayList<Parameter>();
		isAttribute = true;
		XLog endEventsLog = endEventsOnly();
		//classifiers array contains all complex classifiers that can be mapped to the log
		//the attribute is the selected thing from the dropdown regardless of whether it 
		//is a classifier or an attribute
		
		// check whether the selected string is an attribute or a classifier
		for (XEventClassifier c: classifiers) {
			if (c.name().equals(attribute)) {
				//if it is a classifier than create eventclasses object accordingly
				isAttribute = false; // the selected string is a complex classifier				
				xEventClasses = new XEventClasses(c);
				xEventClasses = XEventClasses.deriveEventClasses(c, endEventsLog);				
		
				for (int i = 0; i <= xEventClasses.size() - 1; i++) {
					allValues.add(xEventClasses.getByIndex(i).toString());
				}
				break;
			}
		}
		
		if (isAttribute) {
			//if it is an attribute than create eventclasses object accordingly
			XEventAttributeClassifier attrClassifier = new XEventAttributeClassifier(
					"attrClassifier", attribute);
			
			xEventClasses = new XEventClasses(attrClassifier);
			xEventClasses = XEventClasses.deriveEventClasses(attrClassifier, endEventsLog);
			
			for (int i = 0; i <= xEventClasses.size() - 1; i++) {
				//uncomment to disallow filtering on empty values(non-null but just empty)
				//if (!xEventClasses.getByIndex(i).toString().equals("")) {
				allValues.add(xEventClasses.getByIndex(i).toString());
				//}
			}			
		}
		
		// Create desiredEvents parameter	
		ParameterMultipleFromSet desiredEvents = new ParameterMultipleFromSet(
				"desiredEvents", "Select end event values", new ArrayList<>(), allValues);
		
		// Should you keep empty traces
		ParameterYesNo traceHandling = new ParameterYesNo("traceHandling", 
				"Keep empty traces.", false);
		
		List<Integer> optionsPair = new ArrayList<>();
		optionsPair.add(0);
		optionsPair.add(100);
		ParameterValueFromRange<Integer> threshold = new ParameterValueFromRange<Integer>(
				"threshold", "Frequency threshold", 100, optionsPair, Integer.TYPE);
		
		parameters.add(desiredEvents);
		parameters.add(traceHandling);
		parameters.add(threshold);
		
		// none of the complex classifiers matched the selected values, therefore the 
		// selected string is a (possibly not global) attribute
		if (isAttribute) {
			// should you keep events which do not have the specified attribute
			ParameterYesNo eventHandling = new ParameterYesNo("eventHandling", 
					"Keep events if attribute not specified.", false);
			parameters.add(eventHandling);
			for (XTrace trace : endEventsLog) {
				for (XEvent event : trace) {
					String value;
					if (event.getAttributes().containsKey(attribute)) {
						value = event.getAttributes().get(attribute).toString();
					} else {
						continue;
					}
					if (!allValues.contains(value)) {
						allValues.add(value);
					}
				}
			}
		}
	}
	
	
	public boolean canPopulate(FilterConfigPanelController component) {
		//check whether no params are empty if you populate with the component
		return true;
	};

	public boolean checkValidity(XLog log) {
		return true;
	}
	
	private XLog endEventsOnly() {
		XLog filteredLog = Toolbox.initializeLog(log);
		XFactory factory = XFactoryRegistry.instance().currentDefault();
		for (XTrace trace: this.log) {
			XTrace filteredTrace = factory.createTrace(trace.getAttributes());
			filteredTrace.add(trace.get(trace.size()-1));
			filteredLog.add(filteredTrace);
		}
		return filteredLog;
	}
	
	public String getAttribute() {
		return attribute;
	}
	
	
	@Override
	public NestedFilterConfigPanelController getConfigPanel() {

		NestedFilterConfigPanelController nestedPanel = new NestedFilterConfigPanelController(parameters);
		for( ParameterController controller : nestedPanel.getControllers()) {
			//find threshold parameter controller and add listener to it
			if (controller.getName().equals("threshold")) {
				ParameterValueFromRangeController casted = (ParameterValueFromRangeController) controller;
				Slider slider = casted.getSlider();
				slider.valueProperty().addListener(new ChangeListener<Number>() {

					private ParameterValueFromRange<Integer> threshold;

					@Override
					public void changed(ObservableValue<? extends Number> observable, Number oldValue,
							Number newValue) {
						//find the controller for desired events so that it can change
						for (ParameterController changingController : nestedPanel.getControllers()) {
							if (changingController.getName().equals("desiredEvents")) {
								 ParameterMultipleFromSetController castedChangingController = 
											(ParameterMultipleFromSetController) changingController;
								 //select events corresponding to the slider selection 
								 //using the toolbox function
								 
								 //the parameter is only used because thats the interface in toolbox
								 //but nice solution would also be making it string & number, cos this is kinda workaround
								 ParameterOneFromSet rate = new ParameterOneFromSet(
										 "frequency","frequency","frequency",null );
								 rate.setChosen("Frequency");
								 threshold = new ParameterValueFromRange<Integer>(
											"Frequency threshold", "threshold", 100, null, Integer.TYPE);
								 threshold.setChosen(newValue.intValue());
								 List<String> selection = new ArrayList<String>();

								 
								 selection = Toolbox.computeDesiredEventsFromThreshold(threshold, rate, xEventClasses);
								 castedChangingController.setSelected(selection);
								 
								 ParameterMultipleFromSet castedChangingParameter = (ParameterMultipleFromSet) getParameter("desiredEvents");
								 castedChangingParameter.setChosen(selection);
								 
								
								 
							}	
							
						}
						
					}

				});
			
			}
		}

		return nestedPanel;
	}

}
