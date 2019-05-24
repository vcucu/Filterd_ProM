package org.processmining.filterd.widgets;

import java.io.IOException;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;

public class ParameterValueFromRangeController<N extends Number> extends ParameterController {
	@FXML private Slider slider;
	@FXML private Label label;
	private Class<N> genericTypeClass;
	
	public ParameterValueFromRangeController(String nameDisplayed, String name, N defaultValue, List<N> minMaxPair, Class<N> genericTypeClass) {
		super(name);
		this.genericTypeClass = genericTypeClass;
		// load contents
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/processmining/filterd/widgets/fxml/ParameterValueFromRange.fxml"));
        fxmlLoader.setController(this);
        try {
            contents = (VBox) fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // set specifics
        label.setText(nameDisplayed);
        slider.setMin((Double) minMaxPair.get(0));
        slider.setMax((Double) minMaxPair.get(1));
        slider.setMinorTickCount(4);
        slider.setValue((Double) defaultValue);
        double majorTickUnit = ((Double) minMaxPair.get(1) - (Double) minMaxPair.get(0)) / 4.0;
        slider.setMajorTickUnit(Math.floor(majorTickUnit));
        if(genericTypeClass.equals(Double.TYPE)) {
        	slider.setBlockIncrement(0.1);
        } else if(genericTypeClass.equals(Integer.TYPE)) {
        	slider.setBlockIncrement(1);
        } else {
        	throw new IllegalArgumentException("Supported types are integer and double");
        }
	}
	
	public N getValue() {
		if(genericTypeClass.equals(Double.TYPE)) {
			return (N)(Object) slider.getValue();
        } else if(genericTypeClass.equals(Integer.TYPE)) {
        	return (N)(Object) Math.round(slider.getValue());
        } else {
        	throw new IllegalStateException("Supported types are integer and double");
        }
	}
}