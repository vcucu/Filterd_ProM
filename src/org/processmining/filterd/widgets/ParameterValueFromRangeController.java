package org.processmining.filterd.widgets;

import java.io.IOException;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;

public class ParameterValueFromRangeController extends ParameterController {
	@FXML private Slider slider;
	@FXML private Label label;
	
	public ParameterValueFromRangeController(String title, String id, double defaultValue, List<Double> minMaxPair) {
		super(id);
		// load contents
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/processmining/filterd/widgets/fxml/ParameterValueFromRange.fxml"));
        fxmlLoader.setController(this);
        try {
            contents = (VBox) fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // set specifics
        label.setText(title);
        slider.setMin(minMaxPair.get(0));
        slider.setMax(minMaxPair.get(1));
        slider.setValue(defaultValue);
	}
}