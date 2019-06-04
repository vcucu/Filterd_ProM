package org.processmining.filterd.plugins;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.processmining.contexts.uitopia.annotations.UIExportPlugin;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginLevel;
import org.processmining.framework.plugin.annotations.PluginVariant;

/* This class exports a notebook to the disk. */

@Plugin(name = "Export Notebook", returnLabels = {}, returnTypes = {},
level = PluginLevel.Regular, parameterLabels = { "String", "File"}, userAccessible = true)
@UIExportPlugin(description = "Export Notebook", extension = "xml")
public class NotebookExport {
	
	@UITopiaVariant(affiliation = UITopiaVariant.EHV, author = "", email = "")
	@PluginVariant(requiredParameterLabels = { 0, 1 }, variantLabel = "Export Number File")
	public void export(PluginContext context, String notebook, File file) throws IOException {
		FileWriter fw = new FileWriter(file);
		fw.write(notebook);
        fw.close();
	}
}
