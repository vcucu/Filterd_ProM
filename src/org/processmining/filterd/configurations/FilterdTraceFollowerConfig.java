package org.processmining.filterd.configurations;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.filterd.filters.Filter;
import org.processmining.filterd.gui.AbstractFilterConfigPanelController;
import org.processmining.filterd.gui.FilterConfigPanelController;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterRangeFromRange;
import org.processmining.filterd.parameters.ParameterYesNo;
import org.processmining.filterd.tools.EmptyLogException;
import org.processmining.filterd.widgets.ParameterController;
import org.processmining.filterd.widgets.ParameterMultipleFromSetController;
import org.processmining.filterd.widgets.ParameterOneFromSetController;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;

public class FilterdTraceFollowerConfig extends FilterdAbstractConfig {
	
	Set<String> eventAttributes;

	public FilterdTraceFollowerConfig(XLog log, Filter filterType) throws EmptyLogException {
		super(log, filterType);
		this.log = log;
		
		// Initialize the configuration's parameters list.
		parameters = new ArrayList<>();
		
		// Do this by looping over every trace and collecting its attributes
		// and adding this to the set, except for time:timestamp
		eventAttributes = new HashSet<>();
		for (XTrace trace : log) {
			
			for (XEvent event : trace) {

				for (String key : event.getAttributes().keySet()) {
					if (!key.equals("time:timestamp")) {
						eventAttributes.add(key);
					}
				}
				
			}
		
		}
		
		// Convert the set into an array list because ParameterOneFromSet takes
		// a list as an argument.
		List<String> eventAttributesList = 
				new ArrayList<String>(eventAttributes);
		
		// Create the parameter for selecting the attribute.
		ParameterOneFromSet attributeSelector = 
				new ParameterOneFromSet(
						"attrType", 
						"Select attribute", 
						eventAttributesList.get(0), 
						eventAttributesList);
		
		List<String> selectionTypeList = new ArrayList<String>();
		selectionTypeList.add("Directly followed");
		selectionTypeList.add("Never directly followed");
		selectionTypeList.add("Eventually followed");
		selectionTypeList.add("Never eventually followed");
		
		// Create the parameter for selecting the type.
		ParameterOneFromSet selectionType = new ParameterOneFromSet(
								"Selection type", 
								"Select selection type", 
								selectionTypeList.get(0), 
								selectionTypeList);
		
		Set<String> attributeValues = new HashSet<>();
		
		for (XTrace trace : log) {
			
			for (XEvent event : trace) {
				
				XAttributeMap eventAttrs = event.getAttributes();
				if (eventAttrs.containsKey(eventAttributesList.get(0))) {
					attributeValues.add(eventAttrs.get(eventAttributesList.get(0)).toString());
				}
				
			}
			
		}
		
		// To populate both the reference and follower event values with these
		// attribute values to start with. If the attribute is changed, so will
		// the values in both these parameters.
		List<String> attributeValuesList = new ArrayList<String>(attributeValues);
		
		// Create parameter for reference event values.
		
		ParameterMultipleFromSet referenceParameter = 
				new ParameterMultipleFromSet(
					"attrValues",
					"Desired values:",
					Arrays.asList(attributeValuesList.get(0)),
					attributeValuesList
				);
		
		// Create parameter for follower event values.
		ParameterMultipleFromSet followerParameters = 
				new ParameterMultipleFromSet(
					"attrValues",
					"Desired values:",
					Arrays.asList(attributeValuesList.get(0)),
					attributeValuesList
				);
		
		
		// Create parameter for value matching.
		ParameterYesNo valueMatchingParameter = new ParameterYesNo(
				"Value matching", 
				"Value matching", 
				false);
		
		List<String> sameOrDifferentList = new ArrayList<String>();
		sameOrDifferentList.add("The same value");
		sameOrDifferentList.add("Different values");
		
		// Create parameter for either same value or different value.
		ParameterOneFromSet sameOrDifferentParameter = new ParameterOneFromSet(
				"Same or Different value", 
				"Select same or different value", 
				sameOrDifferentList.get(0), 
				sameOrDifferentList);
		
		// Create parameter for selecting the attribute whose value has to be
		// matched.
		ParameterOneFromSet valueMatchingAttributeParameter = 
				new ParameterOneFromSet(
				"Attribute for value matching", 
				"Select attribute", 
				eventAttributesList.get(0), 
				eventAttributesList);
		
		// Create parameter for a time restriction.
		ParameterYesNo timeRestrictionParameter = new ParameterYesNo(
				"Time restrictions", 
				"Time restrictions", 
				false);
		
		List<String> shorterOrLongerList = new ArrayList<String>();
		shorterOrLongerList.add("Shorter");
		shorterOrLongerList.add("Longer");
		
		// Create parameter for selecting whether the time needs to be longer
		// or shorter than the time selected.
		ParameterOneFromSet shorterOrLongerParameter = new ParameterOneFromSet(
				"Shorter or longer", 
				"Select shorter or longer", 
				shorterOrLongerList.get(0), 
				shorterOrLongerList);
		
	
		// Create parameter for selecting time duration.
		ParameterRangeFromRange<Integer> timeDurationParameter = 
				new ParameterRangeFromRange<Integer>(
				"duration", 
				"Select time duration", 
				Arrays.asList(1, 999), 
				Arrays.asList(1, 999),
				Integer.TYPE);
		
		// Create parameter for selecting the time type.
		ParameterOneFromSet timeTypeParameter = 
				new ParameterOneFromSet(
						"timeType", 
						"Select time type", 
						"millis", 
						Arrays.asList(
								"millis",
								"seconds",
								"minutes",
								"hours",
								"days",
								"weeks",
								"years"));
		
		
		parameters.add(attributeSelector);
		parameters.add(selectionType);
		parameters.add(referenceParameter);
		parameters.add(followerParameters);
		parameters.add(timeRestrictionParameter);
		parameters.add(valueMatchingParameter);
		parameters.add(shorterOrLongerParameter);
		parameters.add(sameOrDifferentParameter);
		parameters.add(timeDurationParameter);
		parameters.add(valueMatchingAttributeParameter);
		parameters.add(timeTypeParameter);
	}

	public boolean canPopulate(FilterConfigPanelController component) {
		//check whether no params are empty if you populate with the component
		return true;
	};

	@Override
	public AbstractFilterConfigPanelController getConfigPanel() {
		FilterConfigPanelController filterConfigPanel = new FilterConfigPanelController(
				"Filter Traces follower filter", 
				parameters, 
				this);
		for(ParameterController parameter : filterConfigPanel.getControllers()) {
			if (parameter.getName().equals("attrType")) {
				ParameterOneFromSetController casted = (ParameterOneFromSetController) parameter;
				ComboBox<String> comboBox = casted.getComboBox();
				comboBox.valueProperty().addListener(new ChangeListener<String>() {
					@Override 
					public void changed(ObservableValue ov, String oldValue, String newValue) {
						final XLog Llog = log;
						List<Parameter> params = parameters;
						if (Llog != null) {
						for (ParameterController changingParameter : filterConfigPanel.getControllers()) {
							
							if (changingParameter.getName().equals("attrValues")) {
								
								ParameterMultipleFromSetController castedChanging = 
										(ParameterMultipleFromSetController) changingParameter;
								Set<String> attributeValues = new HashSet<>();
								
								for (XTrace trace : Llog) {
									
									for (XEvent event : trace) {
										
										XAttributeMap eventAttrs = event.getAttributes();
										if (eventAttrs.containsKey(newValue))
											attributeValues.add(eventAttrs.get(newValue).toString());
									}
								}
								List<String> attributeValuesList = new ArrayList<String>(attributeValues);
								((ParameterMultipleFromSet) params.get(2))
								.setOptions(attributeValuesList);
								((ParameterMultipleFromSet) params.get(2))
								.setChosen(attributeValuesList);
								((ParameterMultipleFromSet) params.get(2))
								.setDefaultChoice(attributeValuesList);
								((ParameterMultipleFromSet) params.get(3))
								.setOptions(attributeValuesList);
								((ParameterMultipleFromSet) params.get(3))
								.setChosen(attributeValuesList);
								((ParameterMultipleFromSet) params.get(3))
								.setDefaultChoice(attributeValuesList);
								castedChanging.changeOptions(attributeValuesList);
							
							}
						}
						
					}
					}
			});
				
		}
		}
		return filterConfigPanel;
	}

	public boolean checkValidity(XLog candidateLog) {
		if( parameters == null || candidateLog.equals(log) )
			return true;
		return false;
	}
}
