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

/**
 * Class representing the plug-in for exporting the Filterd notebook to the
 * workspace.
 */

@Plugin(name = "Export Notebook", returnLabels = {}, returnTypes = {}, level = PluginLevel.Regular, parameterLabels = {
		"String", "File" }, userAccessible = true)
@UIExportPlugin(description = "Export Notebook", extension = "xml")
public class NotebookExport {

	/**
	 * Export plug-in method.
	 * 
	 * @param context
	 *            variable not used
	 * @param notebook
	 *            XML representing the notebook configuration
	 * @param file
	 *            file to which we will write
	 * @throws IOException
	 */
	@UITopiaVariant(affiliation = UITopiaVariant.EHV, author = "", email = "")
	@PluginVariant(requiredParameterLabels = { 0, 1 }, variantLabel = "Export Number File")
	public void export(PluginContext context, String notebook, File file) throws IOException {
		FileWriter fw = new FileWriter(file); // create a file writer with the given file
		fw.write(notebook); // write the notebook (represented as a XML i.e. string)
		fw.close(); // close the file writer
	}
}
