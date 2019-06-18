package org.processmining.filterd.plugins;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.filterd.gui.adapters.ComputationCellModelAdapted;
import org.processmining.filterd.gui.adapters.FilterButtonAdapted;
import org.processmining.filterd.gui.adapters.FilterdAbstractConfigAdapted;
import org.processmining.filterd.gui.adapters.FilterdAbstractConfigAdapter;
import org.processmining.filterd.gui.adapters.FilterdAbstractReferencingConfigAdapted;
import org.processmining.filterd.gui.adapters.FilterdTraceStartEventCategoricalConfigAdapted;
import org.processmining.filterd.gui.adapters.NotebookModelAdapted;
import org.processmining.filterd.gui.adapters.TextCellModelAdapted;
import org.processmining.filterd.parameters.Parameter;
import org.processmining.filterd.parameters.ParameterMultipleFromSet;
import org.processmining.filterd.parameters.ParameterOneFromSet;
import org.processmining.filterd.parameters.ParameterRangeFromRange;
import org.processmining.filterd.parameters.ParameterText;
import org.processmining.filterd.parameters.ParameterValueFromRange;
import org.processmining.filterd.parameters.ParameterYesNo;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;

@Plugin(name = "Load Notebook", returnLabels = {"Output Notebook"}, returnTypes = {NotebookModelAdapted.class}, parameterLabels = {
		"String", "XLog"}, userAccessible = true)
public class NotebookLoader {
	@UITopiaVariant(affiliation = UITopiaVariant.EHV, author = "M. Diea & T. Stoenescu & E. Samuels", email = "sl")
	@PluginVariant(variantLabel = "Filterd plug-in, setup wizard", requiredParameterLabels = {0, 1})
	public NotebookModelAdapted load(UIPluginContext context, String imported, XLog log) {
		NotebookModelAdapted adaptedModel;
		try {
			// read the XML.
			// add all the classes which have a XmlRootElement annotation in the newInstance method.
			JAXBContext jaxbContext = JAXBContext.newInstance(NotebookModelAdapted.class, TextCellModelAdapted.class,
					ComputationCellModelAdapted.class, FilterButtonAdapted.class, FilterdAbstractConfigAdapted.class,
					Parameter.class, ParameterMultipleFromSet.class, ParameterOneFromSet.class,
					ParameterRangeFromRange.class, ParameterText.class, ParameterValueFromRange.class, ParameterYesNo.class,
					FilterdAbstractReferencingConfigAdapted.class, FilterdAbstractConfigAdapted.class,
					FilterdTraceStartEventCategoricalConfigAdapted.class); // Create JAXB Context.
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			
			StringReader reader = new StringReader(imported);
			// set the initial input for the abstract configs.
			FilterdAbstractConfigAdapter.setInitialInput(log);
			adaptedModel = (NotebookModelAdapted) jaxbUnmarshaller.unmarshal(reader);
			
			// embed the initial input to the adaptedModel.
			adaptedModel.setInitialInput(log);
			
			
			return adaptedModel;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
