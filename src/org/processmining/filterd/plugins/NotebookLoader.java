package org.processmining.filterd.plugins;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.filterd.gui.NotebookModel;
import org.processmining.filterd.gui.adapters.AbstractJAXBAdapter;
import org.processmining.filterd.gui.adapters.ComputationCellModelAdapted;
import org.processmining.filterd.gui.adapters.NotebookModelAdapted;
import org.processmining.filterd.gui.adapters.NotebookModelAdapter;
import org.processmining.filterd.gui.adapters.TextCellModelAdapted;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;

@Plugin(name = "Load Notebook", returnLabels = {"Output Notebook"}, returnTypes = {NotebookModel.class}, parameterLabels = {
		"String", "XLog"}, userAccessible = true)
public class NotebookLoader {
	@UITopiaVariant(affiliation = UITopiaVariant.EHV, author = "M. Diea & T. Stoenescu & E. Samuels", email = "sl")
	@PluginVariant(variantLabel = "Filterd plug-in, setup wizard", requiredParameterLabels = {0, 1})
	public NotebookModel load(UIPluginContext context, String imported, XLog log) {
		NotebookModel notebookModel;
		try {
			// set the static loading variables.
			AbstractJAXBAdapter.setContext(context);
			AbstractJAXBAdapter.setInitialInput(log);
			
			// read the XML.
			// add all the classes which have a XmlRootElement annotation in the newInstance method.
			JAXBContext jaxbContext= JAXBContext.newInstance(NotebookModelAdapted.class, TextCellModelAdapted.class, ComputationCellModelAdapted.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			
			StringReader reader = new StringReader(imported);
			NotebookModelAdapted adaptedModel = (NotebookModelAdapted) jaxbUnmarshaller.unmarshal(reader);
			
			// convert the adapted model to a notebook model.
			NotebookModelAdapter adapter = new NotebookModelAdapter();		
			notebookModel = adapter.unmarshal(adaptedModel);
		} catch (Exception e) {
			e.printStackTrace();
			notebookModel = new NotebookModel(context, log, null);
		}
		return notebookModel;
	}
}
