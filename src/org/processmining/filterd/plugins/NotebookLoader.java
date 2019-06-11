package org.processmining.filterd.plugins;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.filterd.gui.adapters.ComputationCellModelAdapted;
import org.processmining.filterd.gui.adapters.NotebookModelAdapted;
import org.processmining.filterd.gui.adapters.TextCellModelAdapted;
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
			JAXBContext jaxbContext= JAXBContext.newInstance(NotebookModelAdapted.class, TextCellModelAdapted.class, ComputationCellModelAdapted.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			
			StringReader reader = new StringReader(imported);
			adaptedModel = (NotebookModelAdapted) jaxbUnmarshaller.unmarshal(reader);
			
			// embed the initial input to the adaptedModel
			adaptedModel.setInitialInput(log);
			
//			// convert the adapted model to a notebook model.
//			NotebookModelAdapter adapter = new NotebookModelAdapter();		
//			notebookModel = adapter.unmarshal(adaptedModel);
			return adaptedModel;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
