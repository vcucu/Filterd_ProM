package org.processmining.filterd.plugins;

import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.filterd.gui.NotebookModel;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;

@Plugin(name = "Load Notebook", returnLabels = {"Output Notebook"}, returnTypes = {NotebookModel.class}, parameterLabels = {
		"Notebook", "XLog"}, userAccessible = true)
public class NotebookLoader {
	@UITopiaVariant(affiliation = UITopiaVariant.EHV, author = "M. Diea & T. Stoenescu", email = "sl")
	@PluginVariant(variantLabel = "Filterd plug-in, setup wizard", requiredParameterLabels = {0, 1})
	public NotebookModel load(UIPluginContext context, NotebookModel imported, XLog log) {
		NotebookModel notebook = new NotebookModel(context, log);
		notebook.addCells(imported.getCells());
		return notebook;
	}
}
