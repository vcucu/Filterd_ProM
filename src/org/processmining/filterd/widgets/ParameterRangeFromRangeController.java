package org.processmining.filterd.widgets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.controlsfx.control.RangeSlider;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ParameterRangeFromRangeController<N extends Number> extends ParameterController {
	@FXML private RangeSlider slider;
	@FXML private Label label;
	private Class<N> genericTypeClass;
	
	public ParameterRangeFromRangeController(String nameDisplayed, String name, List<N> defaultValue, List<N> minMaxPair, Class<N> genericTypeClass) {
		super(name);
		this.genericTypeClass = genericTypeClass;
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
		double majorTickUnit = ((double) minMaxPair.get(1) - (double) minMaxPair.get(0)) / 4.0;
		slider.setMajorTickUnit(majorTickUnit);
        slider.setMin((double) minMaxPair.get(0));
        slider.setMax((double) minMaxPair.get(1));
        slider.setLowValue((double) defaultValue.get(0));
        slider.setHighValue((double) defaultValue.get(1));
        if(genericTypeClass.equals(Double.TYPE)) {
        	slider.setBlockIncrement(0.1);
        } else if(genericTypeClass.equals(Integer.TYPE)) {
        	slider.setBlockIncrement(1);
        } else {
        	throw new IllegalArgumentException("Supported types are integer and double");
        }
	}
	
	public List<N> getValue() {
		List<N> value = new ArrayList<>();
		if(genericTypeClass.equals(Double.TYPE)) {
			value.add((N)(Object) slider.getLowValue());
			value.add((N)(Object) slider.getHighValue());
        } else if(genericTypeClass.equals(Integer.TYPE)) {
        	value.add((N)(Object) Math.round(slider.getLowValue()));
    		value.add((N)(Object) Math.round(slider.getHighValue()));
        } else {
        	throw new IllegalArgumentException("Supported types are integer and double");
        }
		return value;
	}
}
