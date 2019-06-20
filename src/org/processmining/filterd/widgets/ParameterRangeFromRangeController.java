package org.processmining.filterd.widgets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.controlsfx.control.RangeSlider;
import org.python.icu.text.DecimalFormat;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Range from range parameter UI counterpart.
 * 
 * @param <T>
 *            type of the parameter (can be either Integer or Double)
 * 
 * @author Filip Davidovic
 */
public class ParameterRangeFromRangeController<N extends Number> extends ParameterController {
	@FXML
	private RangeSlider slider; // slider to select the range from a range
	@FXML
	private Label label; // description of the parameter
	@FXML
	private Label lowValueLabel; // label for the current low value of the slider (better UX)
	@FXML
	private Label highValueLabel; // label for the current high value of the slider (better UX)
	private Class<N> genericTypeClass; // generic class (there is no way to get type of N at runtime so this variable is populated in the constructor)
	private boolean isActingLikeInteger; // boolean stating whether the slider is behaving like an integer slider
	private boolean isTimeframe;
	private ChangeListener<N> lowValueChangeListener; // change listener for the low value
	private ChangeListener<N> highValueChangeListener; // change listener for the high value
	List<String> times; // needed for timeframe

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
	 *            low and high values of the slider which are selected by
	 *            default
	 * @param minMaxPair
	 *            minimum and maximum values of the slider
	 * @param genericTypeClass
	 *            type of the generic class i.e. N (used to set the behavior of
	 *            the slider)
	 */
	public ParameterRangeFromRangeController(String nameDisplayed, String name, List<N> defaultValue,
			List<N> minMaxPair, Class<N> genericTypeClass) {
		super(name);
		this.genericTypeClass = genericTypeClass;
		// check if timeframe 
		if (name.equals("time-range")) {
			isTimeframe = true;
		} else {
			isTimeframe = false;
		}
		// load contents
		FXMLLoader fxmlLoader = new FXMLLoader(
				getClass().getResource("/org/processmining/filterd/widgets/fxml/ParameterRangeFromRange.fxml"));
		fxmlLoader.setController(this);
		try {
			contents = (VBox) fxmlLoader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		// set specifics
		label.setText(nameDisplayed); // set the description of the parameter
		// set the slider
		setSliderConfig(defaultValue, minMaxPair); // set the slider configuration
		if (!genericTypeClass.equals(Double.TYPE) && !genericTypeClass.equals(Integer.TYPE)) {
			// this component only supports integer and double numbers
			throw new IllegalArgumentException("Supported types are integer (Integer.TYPE) and double (Double.TYPE)");
		}
		// set listeners to change the slider labels when the slider value changes
		slider.lowValueProperty().addListener(new ChangeListener<Number>() {

			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (genericTypeClass.equals(Double.TYPE)) {
					// slider is for doubles 
					if (isActingLikeInteger) {
						lowValueLabel.setText(Integer.toString(newValue.intValue()));
					} else {
						DecimalFormat df = new DecimalFormat("0.00");
						lowValueLabel.setText(df.format(newValue));
					}
				} else if (genericTypeClass.equals(Integer.TYPE)) {
					// slider is for integers
					if (!isTimeframe) {
						lowValueLabel.setText(Integer.toString(newValue.intValue()));
					} else {
						lowValueLabel.setText(times.get(newValue.intValue()));
					}
				}
			}

		});
		slider.highValueProperty().addListener(new ChangeListener<Number>() {

			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (genericTypeClass.equals(Double.TYPE)) {
					if (isActingLikeInteger) {
						highValueLabel.setText(Integer.toString(newValue.intValue()));
					} else {
						DecimalFormat df = new DecimalFormat("0.00");
						highValueLabel.setText(df.format(newValue));
					}
				} else if (genericTypeClass.equals(Integer.TYPE)) {
					if (!isTimeframe) {
						highValueLabel.setText(Integer.toString(newValue.intValue()));
					} else {
						highValueLabel.setText(times.get(newValue.intValue()));
					}
				}
			}

		});
		this.lowValueChangeListener = new ChangeListener<N>() {

			public void changed(ObservableValue<? extends N> observable, N oldValue, N newValue) {
				slider.setLowValue(Math.round(newValue.doubleValue()));
			}
		};
		this.highValueChangeListener = new ChangeListener<N>() {

			public void changed(ObservableValue<? extends N> observable, N oldValue, N newValue) {
				slider.setHighValue(Math.round(newValue.doubleValue()));
			}
		};
		this.isActingLikeInteger = false;
		this.times = new ArrayList<>();
	}

	public List<N> getValue() {
		List<N> value = new ArrayList<>();
		if (genericTypeClass.equals(Double.TYPE)) {
			value.add((N) (new Double(slider.getLowValue())));
			value.add((N) (new Double(slider.getHighValue())));
		} else if (genericTypeClass.equals(Integer.TYPE)) {
			value.add((N) new Integer((new Double(slider.getLowValue())).intValue()));
			value.add((N) new Integer((new Double(slider.getHighValue())).intValue()));
		} else {
			throw new IllegalArgumentException("Supported types are integer (Integer.TYPE) and double (Double.TYPE)");
		}
		return value;
	}

	/**
	 * Set the behavior and look of the slider based on the passed parameters.
	 * One thing to note is that the slider only works with double values. If we
	 * want to use integer values we make it behave like it works with integer
	 * values.
	 * 
	 * @param defaultValue
	 *            low and high values that should be selected by default
	 * @param minMaxPair
	 *            minimum and maximum values of the slider
	 */
	public void setSliderConfig(List<N> defaultValue, List<N> minMaxPair) {
		if (genericTypeClass.equals(Double.TYPE)) {
			// slider should behave like a double slider
			double majorTickUnit = ((Double) minMaxPair.get(1) - (Double) minMaxPair.get(0)) / 4.0;
			majorTickUnit = Math.max(1, Math.floor(majorTickUnit)); //unit must be > 0
			// set the minimum and maximum values of the slider
			// Note: due to a bug in javaFX the max needs to be set before a min can be set.
			slider.setMax((Double) minMaxPair.get(1));
			slider.setMin((Double) minMaxPair.get(0));
			// set the default values of the slider
			// Note: due to a bug in javaFX the HighValue needs to be set before a LowValue can be set.
			slider.setHighValue((Double) defaultValue.get(1));
			slider.setLowValue((Double) defaultValue.get(0));
			// make the slider "continuous"
			slider.setBlockIncrement(0.1);
			// format the labels appropriately 
			if (isActingLikeInteger) {
				highValueLabel.setText(Integer.toString(defaultValue.get(1).intValue()));
				lowValueLabel.setText(Integer.toString(defaultValue.get(0).intValue()));
			} else {
				DecimalFormat df = new DecimalFormat("0.00");
				highValueLabel.setText(df.format((Double) defaultValue.get(1)));
				lowValueLabel.setText(df.format((Double) defaultValue.get(0)));
			}
			slider.setMajorTickUnit(majorTickUnit);
		} else if (genericTypeClass.equals(Integer.TYPE)) {
			// slider should behave like an integer slider
			
			double majorTickUnit = (minMaxPair.get(1).doubleValue() - minMaxPair.get(0).doubleValue()) / 4.0;
			majorTickUnit = Math.max(1, Math.floor(majorTickUnit)); //unit must be > 0
			slider.setMajorTickUnit(majorTickUnit);
			// set the minimum and maximum values of the slider
			// Note: due to a bug in javaFX the max needs to be set before a min can be set.
			slider.setMax(minMaxPair.get(1).doubleValue());
			slider.setMin(minMaxPair.get(0).doubleValue());
			// set the default values of the slider
			// Note: due to a bug in javaFX the HighValue needs to be set before a LowValue can be set.
			slider.setHighValue(defaultValue.get(1).doubleValue());
			slider.setLowValue(defaultValue.get(0).doubleValue()); 
			// make the slider "discrete"
			slider.setBlockIncrement(1);
			// format the labels appropriately 
			if (!isTimeframe) {
				lowValueLabel.setText(Integer.toString(defaultValue.get(0).intValue()));
				highValueLabel.setText(Integer.toString(defaultValue.get(1).intValue()));
			} else {
				lowValueLabel.setText("");
				highValueLabel.setText("");
			}
		}
	}

	public void actLikeDouble(List<N> minMaxPair) {
		double majorTickUnit = ((Double) minMaxPair.get(1) - (Double) minMaxPair.get(0)) / 4.0;
		slider.setMajorTickUnit(majorTickUnit);
		slider.setBlockIncrement(0.1);
		slider.lowValueProperty()
				.addListener((obs, oldval, newVal) -> slider.setLowValue(Math.round(newVal.doubleValue())));
		slider.lowValueProperty().removeListener((ChangeListener<? super Number>) this.lowValueChangeListener);
		slider.highValueProperty().removeListener((ChangeListener<? super Number>) this.highValueChangeListener);
		this.isActingLikeInteger = false;
	}

	public void actLikeInteger(List<N> minMaxPair) {
		double majorTickUnit = (minMaxPair.get(1).doubleValue() - minMaxPair.get(0).doubleValue()) / 4.0;
		majorTickUnit = Math.max(1, Math.floor(majorTickUnit)); //unit must be > 0
		slider.setMajorTickUnit(majorTickUnit);
		slider.setBlockIncrement(1.0);
		slider.lowValueProperty().addListener((ChangeListener<? super Number>) this.lowValueChangeListener);
		slider.highValueProperty().addListener((ChangeListener<? super Number>) this.highValueChangeListener);
		this.isActingLikeInteger = true;
	}

	public void setTimeframe() {
		slider.setShowTickLabels(false);
		slider.setShowTickMarks(false);
		lowValueLabel.setText(times.get(0));
		highValueLabel.setText(times.get(times.size() - 1));
		//TO DO: set the labels such that date fits
	}

	public void setTimeframe(boolean isTimeframe) {
		this.isTimeframe = isTimeframe;
	}

	public boolean getTimeframe() {
		return this.isTimeframe;
	}

	public void setTimes(List<String> times) {
		this.times = times;
	}
}
