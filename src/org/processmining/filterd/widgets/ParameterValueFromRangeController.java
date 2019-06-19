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

/**
 * Value from range parameter UI counterpart.
 *
 * @param <N>
 *            type of the parameter (can be either Integer or Double)
 * 
 * @author Filip Davidovic
 */
public class ParameterValueFromRangeController<N extends Number> extends ParameterController {
	@FXML
	private Slider slider; // slider to select the value from a range
	@FXML
	private Label label; // description of the parameter
	private Class<N> genericTypeClass; // generic class (there is no way to get type of N at runtime so this variable is populated in the constructor)
	@FXML
	private Label valueLabel; // label for the current value of the slider (better UX)

	/**
	 * Default constructor which should be used in all actual code.
	 * 
	 * @param nameDisplayed
	 *            description of the parameter
	 * @param name
	 *            unique identified of the parameter (used to map UI parameter
	 *            to actual parameter in populate method of the filter
	 *            configuration)
	 * @param defaultValue
	 *            value of the slider which is selected by default
	 * @param minMaxPair
	 *            minimum and maximum values of the slider
	 * @param genericTypeClass
	 *            type of the generic class i.e. N (used to set the behavior of
	 *            the slider)
	 */
	public ParameterValueFromRangeController(String nameDisplayed, String name, N defaultValue, List<N> minMaxPair,
			Class<N> genericTypeClass) {
		super(name);
		if (!genericTypeClass.equals(Double.TYPE) && !genericTypeClass.equals(Integer.TYPE)) {
			// this component only supports integer and double numbers
			throw new IllegalArgumentException("Supported types are integer (Integer.TYPE) and double (Double.TYPE)");
		}
		this.genericTypeClass = genericTypeClass;
		// load UI contents
		FXMLLoader fxmlLoader = new FXMLLoader(
				getClass().getResource("/org/processmining/filterd/widgets/fxml/ParameterValueFromRange.fxml"));
		fxmlLoader.setController(this);
		try {
			contents = (VBox) fxmlLoader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		// set specifics
		label.setText(nameDisplayed); // set the description of the parameter
		// set slider
		setSliderConfig(defaultValue, minMaxPair); // set the slider configuration
		// set change listener for slider label i.e. update the value label whenever the slider value changes
		slider.valueProperty().addListener(new ChangeListener<Number>() {

			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (genericTypeClass.equals(Double.TYPE)) {
					// if the type is double we want 2 decimal places
					DecimalFormat df = new DecimalFormat("0.00");
					valueLabel.setText(df.format((Double) newValue)); // set the value label based on the new value
				} else if (genericTypeClass.equals(Integer.TYPE)) {
					// if the type is integer we do not want any decimal places
					valueLabel.setText(Integer.toString(newValue.intValue())); // set the value label based on the new value
				}
			}
		});
	}

	/**
	 * Set the behavior and look of the slider based on the passed parameters.
	 * One thing to note is that the slider only works with double values.
	 * If we want to use integer values we make it behave like it works with integer values.
	 * 
	 * @param defaultValue
	 *            value of the slider that is selected by default
	 * @param minMaxPair
	 *            minimum and maximum values of the slider
	 */
	public void setSliderConfig(N defaultValue, List<N> minMaxPair) {
		if (genericTypeClass.equals(Double.TYPE)) {
			// if the type of the slider is double we want continuous increments and decimal value formatting
			// set minimum and maximum values for the slider
			slider.setMin((Double) minMaxPair.get(0));
			slider.setMax((Double) minMaxPair.get(1));
			// set the number of ticks
			slider.setMinorTickCount(4);
			// set the default value as the current value
			slider.setValue((Double) defaultValue);
			double majorTickUnit = ((Double) minMaxPair.get(1) - (Double) minMaxPair.get(0)) / 4.0;
			slider.setMajorTickUnit(Math.max(1, Math.floor(majorTickUnit)));
			// format the value label with two decimal places
			DecimalFormat df = new DecimalFormat("0.00");
			valueLabel.setText(df.format((Double) defaultValue));
		} else if (genericTypeClass.equals(Integer.TYPE)) {
			// if the type of the slider is double we want discrete increments and integer value formatting
			// set minimum and maximum values for the slider
			slider.setMin(minMaxPair.get(0).doubleValue());
			slider.setMax(minMaxPair.get(1).doubleValue());
			// set the number of ticks
			slider.setMinorTickCount(4);
			// set the default value as the current value
			slider.setValue(defaultValue.doubleValue());
			int majorTickUnit = (int) (((Integer) minMaxPair.get(1) - (Integer) minMaxPair.get(0)) / 4.0);
			slider.setMajorTickUnit(Math.max(1, majorTickUnit));
			// format the value label as integer value
			valueLabel.setText(Integer.toString(defaultValue.intValue()));
		}
	}

	/**
	 * Getter for the selected value of the slider.
	 *  
	 * @return selected value of the slider
	 */
	public N getValue() {
		if (genericTypeClass.equals(Double.TYPE)) {
			// return the value as a double
			return (N) (new Double(slider.getValue()));
		} else if (genericTypeClass.equals(Integer.TYPE)) {
			// return the value as an integer
			return (N) new Integer((new Double(slider.getValue())).intValue());
		} else {
			// should never be reached (this is checked in the constructor)
			throw new IllegalArgumentException("Supported types are integer (Integer.TYPE) and double (Double.TYPE)");
		}
	}

	/**
	 * Getter for the slider UI element.
	 * Used by filter configurations to further customize the behavior of this component. 
	 * 
	 * @return slider UI element
	 */
	public Slider getSlider() {
		return this.slider;
	}

}