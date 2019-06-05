package org.processmining.filterd.widgets;

import java.io.IOException;
import java.util.List;

import org.python.icu.text.DecimalFormat;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;

public class ParameterValueFromRangeController<N extends Number> extends ParameterController {
	@FXML private Slider slider;
	@FXML private Label label;
	private Class<N> genericTypeClass;
	@FXML private Label valueLabel;
	
	public ParameterValueFromRangeController(String nameDisplayed, String name, N defaultValue, List<N> minMaxPair, Class<N> genericTypeClass) {
		super(name);
		if(!genericTypeClass.equals(Double.TYPE) && !genericTypeClass.equals(Integer.TYPE)) {
			throw new IllegalArgumentException("Supported types are integer (Integer.TYPE) and double (Double.TYPE)");
        }
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
        // set slider
        setSliderConfig(defaultValue, minMaxPair);
        // set change listener for slider label
        slider.valueProperty().addListener(new ChangeListener<Number>() {

			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if(genericTypeClass.equals(Double.TYPE)) {
		            DecimalFormat df = new DecimalFormat("0.00");
		            valueLabel.setText(df.format((Double) newValue));
		        } else if(genericTypeClass.equals(Integer.TYPE)) {
		            valueLabel.setText(Integer.toString(newValue.intValue()));
		        }
			}
        });
	}
	
	public void setSliderConfig(N defaultValue, List<N> minMaxPair) {
		if(genericTypeClass.equals(Double.TYPE)) {
        	slider.setMin((Double) minMaxPair.get(0));
            slider.setMax((Double) minMaxPair.get(1));
            slider.setMinorTickCount(4);
            slider.setValue((Double) defaultValue);
            double majorTickUnit = ((Double) minMaxPair.get(1) - (Double) minMaxPair.get(0)) / 4.0;
            slider.setMajorTickUnit(Math.floor(majorTickUnit));
            DecimalFormat df = new DecimalFormat("0.00");
            valueLabel.setText(df.format((Double) defaultValue));
        } else if(genericTypeClass.equals(Integer.TYPE)) {
        	slider.setMin(minMaxPair.get(0).doubleValue());
            slider.setMax(minMaxPair.get(1).doubleValue());
            slider.setMinorTickCount(4);
            slider.setValue(defaultValue.doubleValue());
            int majorTickUnit = (int) (((Integer) minMaxPair.get(1) - (Integer) minMaxPair.get(0)) / 4.0);
            slider.setMajorTickUnit(majorTickUnit);
            valueLabel.setText(Integer.toString(defaultValue.intValue()));
        }
	}
	
	public N getValue() {
		if(genericTypeClass.equals(Double.TYPE)) {
        	return (N) (new Double(slider.getValue()));
        } else if(genericTypeClass.equals(Integer.TYPE)) {
        	return (N) new Integer((new Double(slider.getValue())).intValue());
        } else {
        	throw new IllegalArgumentException("Supported types are integer (Integer.TYPE) and double (Double.TYPE)");
        }
	}
	
	public Slider getSlider() {
		return this.slider;
	}

}