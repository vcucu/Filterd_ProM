package org.processmining.filterd.plugins;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import org.processmining.contexts.uitopia.annotations.UIImportPlugin;
import org.processmining.filterd.gui.CellModel;
import org.processmining.filterd.gui.NotebookModel;
import org.processmining.framework.abstractplugins.AbstractImportPlugin;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginLevel;

import javafx.collections.FXCollections;

@Plugin(name = "Import a notebook file", level = PluginLevel.PeerReviewed, parameterLabels = {
		"Filename" }, returnLabels = { "Imported notebook File" }, returnTypes = { NotebookModel.class })
@UIImportPlugin(description = "NotebookModel file", extensions = { "notebook" })
public class NotebookImport extends AbstractImportPlugin {

	@Override
	protected NotebookModel importFromStream(final PluginContext context, final InputStream input,
			final String filename, final long fileSizeInBytes) throws Exception {
		context.getFutureResult(0).setLabel("Imported Notebook: " + filename);

		FileInputStream fis = new FileInputStream(getFile().toPath().toString());
		ObjectInputStream ois = new ObjectInputStream(fis);
		ArrayList<CellModel> cells = (ArrayList<CellModel>) ois.readObject();
		ois.close();

		NotebookModel notebook = new NotebookModel(context);
		notebook.addCells(FXCollections.observableArrayList(cells));

		return notebook;
	}
}
