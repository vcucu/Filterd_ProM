package org.processmining.filterd.gui;

import java.util.ArrayList;
import java.util.List;

import org.processmining.filterd.parameters.Parameter;

import javafx.geometry.Insets;
import javafx.scene.layout.VBox;

/**
 * Configuration panel for referenceable filter configurations. This panel is
 * shown in the nested container of parameter one from set extended UI
 * component.
 * 
 * @author Filip Davidovic
 */
public class NestedFilterConfigPanelController extends AbstractFilterConfigPanelController {

	/**
	 * Default constructor which should be used in all actual code.
	 * 
	 * @param parameters
	 *            list of parameters that should be shown in this panel
	 */
	public NestedFilterConfigPanelController(List<Parameter> parameters) {
		controllers = new ArrayList<>();
		root = new VBox();
		VBox.setMargin(root, new Insets(5.0)); // add a margin to the contents so that it is more readable
		populateFromParameters(parameters); // create UI components from the parameter list
	}

	@Override
	public VBox getNextContainer(Parameter param) {
		return root; // always return the root container (this is the only container)
	}

}
