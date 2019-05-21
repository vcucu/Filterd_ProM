package org.processmining.filterd.widgets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.controlsfx.control.RangeSlider;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ParameterRangeFromRangeController extends ParameterController {
	@FXML private RangeSlider slider;
	@FXML private Label label;
	
	public ParameterRangeFromRangeController(String nameDisplayed, String name, List<Double> defaultValue, List<Double> minMaxPair) {
		super(name);
		// load contents
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/processmining/filterd/widgets/fxml/ParameterRangeFromRange.fxml"));
        fxmlLoader.setController(this);
        try {
            contents = (VBox) fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // set specifics
        label.setText(nameDisplayed);
		slider.setMinorTickCount(4);
		double majorTickUnit = (minMaxPair.get(1) - minMaxPair.get(0)) / 4.0;
		slider.setMajorTickUnit(majorTickUnit);
        slider.setMin(minMaxPair.get(0));
        slider.setMax(minMaxPair.get(1));
        slider.setLowValue(defaultValue.get(0));
        slider.setHighValue(defaultValue.get(1));
	}
	
	public List<Double> getValue() {
		List<Double> value = new ArrayList<>();
		value.add(slider.getLowValue());
		value.add(slider.getHighValue());
		return value;
	}
}
