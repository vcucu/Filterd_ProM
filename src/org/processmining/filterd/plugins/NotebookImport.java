package org.processmining.filterd.plugins;

import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.processmining.contexts.uitopia.annotations.UIImportPlugin;
import org.processmining.framework.abstractplugins.AbstractImportPlugin;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginLevel;

@Plugin(name = "Import a notebook file", level = PluginLevel.PeerReviewed, parameterLabels = {
		"Filename" }, returnLabels = { "Imported notebook File" }, returnTypes = { String.class })
@UIImportPlugin(description = "NotebookModel file", extensions = { "xml" })
public class NotebookImport extends AbstractImportPlugin {

	@Override
	protected String importFromStream(final PluginContext context, final InputStream input,
			final String filename, final long fileSizeInBytes) throws Exception {
		
		context.getFutureResult(0).setLabel("Imported Notebook: " + filename); // set the name that will appear in the workspace.
		FileInputStream fis = new FileInputStream(getFile().toPath().toString()); // open the file.
		String xml = IOUtils.toString(fis, "UTF-8"); //convert the file to a string.
		fis.close(); // close the file.

		return xml;
	}
}