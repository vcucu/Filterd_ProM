package org.processmining.filterd.configurations;
import org.processmining.filterd.parameters.*;
import org.processmining.filterd.widgets.*;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;

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

public class FilterdTraceTrimConfig extends FilterdAbstractConfig {
	Set<String> eventAttributes;
	
	public FilterdTraceTrimConfig(XLog log, Filter filterType) {
		super(log, filterType);
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
			selectionTypeList.add("Trim longest");
			selectionTypeList.add("Trim first");
			
			// Create the parameter for selecting the type.
			ParameterOneFromSet selectionType = new ParameterOneFromSet(
									"followType", 
									"Select trim type", 
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
			ParameterMultipleFromSet firstEvents = 
					new ParameterMultipleFromSet(
						"firstattrValues",
						"Desired values:",
						Arrays.asList(attributeValuesList.get(0)),
						attributeValuesList
					);
			
			// Create parameter for follower event values.
			ParameterMultipleFromSet endEvents = 
					new ParameterMultipleFromSet(
						"endattrValues",
						"Desired values:",
						Arrays.asList(attributeValuesList.get(0)),
						attributeValuesList
					);
			
			parameters = Arrays.asList(attributeSelector, selectionType,
					firstEvents, endEvents); 
	}

	
	public boolean canPopulate(FilterConfigPanelController component) {
		// TODO Auto-generated method stub
		return true;
	}

	public FilterConfigPanelController getConfigPanel() {
		FilterConfigPanelController filterConfigPanel =
				new FilterConfigPanelController("Trace Trim Configuration", 
				parameters, this);
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
							
							if (changingParameter.getName().equals("firstattrValues")) {
								
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
								castedChanging.changeOptions(attributeValuesList);
							
							}
							if (changingParameter.getName().equals("endattrValues")) {
								
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
		if (parameters == null || candidateLog.equals(log))
			return true;
		Set<String> cEventAttributes = new HashSet<>();
		for (XTrace trace : log) {
			
			for (XEvent event : trace) {
				
				eventAttributes.addAll(event.getAttributes().keySet());
				
			}		
		}
		if (!cEventAttributes.contains(((ParameterOneFromSet) parameters.get(0))
				.getChosen()))
			return false;
		return true;	
	}

}
