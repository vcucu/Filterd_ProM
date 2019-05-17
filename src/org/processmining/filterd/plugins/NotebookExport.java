package org.processmining.filterd.plugins;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import org.processmining.contexts.uitopia.annotations.UIExportPlugin;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.filterd.gui.CellModel;
import org.processmining.filterd.gui.NotebookModel;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginLevel;
import org.processmining.framework.plugin.annotations.PluginVariant;

/* This class exports a notebook to the disk. */

@Plugin(name = "Export Notebook", returnLabels = {}, returnTypes = {},
level = PluginLevel.Regular, parameterLabels = { "NotebookModel", "File"}, userAccessible = true)
@UIExportPlugin(description = "Export Notebook", extension = "notebook")
public class NotebookExport {
	
	@UITopiaVariant(affiliation = UITopiaVariant.EHV, author = "", email = "")
	@PluginVariant(requiredParameterLabels = { 0, 1 }, variantLabel = "Export Number File")
	public void export(PluginContext context, NotebookModel notebook, File file) throws IOException {
		FileOutputStream out = new FileOutputStream(file);
		ObjectOutputStream objectOut = new ObjectOutputStream(out);
		ArrayList<CellModel> cells = new ArrayList<CellModel>(notebook.getCells());
        objectOut.writeObject(cells);
        objectOut.close();
	}
}
