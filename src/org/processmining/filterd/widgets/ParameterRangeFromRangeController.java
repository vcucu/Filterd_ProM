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

public class ParameterRangeFromRangeController<N extends Number> extends ParameterController {
	@FXML
	private RangeSlider slider;
	@FXML
	private Label label;
	@FXML
	private Label lowValueLabel;
	@FXML
	private Label highValueLabel;
	private Class<N> genericTypeClass;
	private boolean isActingLikeInteger;
	private boolean isTimeframe; 
	private ChangeListener<N> lowValueChangeListener;
	private ChangeListener<N> highValueChangeListener;
	List<String> times; // needed for timeframe

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
		// set the parameter label
		label.setText(nameDisplayed);
		// set the slider
		setSliderConfig(defaultValue, minMaxPair);
		if (!genericTypeClass.equals(Double.TYPE) && !genericTypeClass.equals(Integer.TYPE)) {
			throw new IllegalArgumentException("Supported types are integer (Integer.TYPE) and double (Double.TYPE)");
		}
		// set listeners for slider labels
		slider.lowValueProperty().addListener(new ChangeListener<Number>() {

			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (genericTypeClass.equals(Double.TYPE)) {
					if (isActingLikeInteger) {
						lowValueLabel.setText(Integer.toString(newValue.intValue()));
					} else {
						DecimalFormat df = new DecimalFormat("0.00");
						lowValueLabel.setText(df.format(newValue));
					}
				} else if (genericTypeClass.equals(Integer.TYPE)) {
					if (!isTimeframe) {
						lowValueLabel.setText(Integer.toString(newValue.intValue()));
					}
					else {
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
					}
					else {
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

	public void setSliderConfig(List<N> defaultValue, List<N> minMaxPair) {
		if (genericTypeClass.equals(Double.TYPE)) {
			double majorTickUnit = ((Double) minMaxPair.get(1) - (Double) minMaxPair.get(0)) / 4.0;
			slider.setMajorTickUnit(majorTickUnit);
			slider.setMin((Double) minMaxPair.get(0));
			slider.setMax((Double) minMaxPair.get(1));
			slider.setLowValue((Double) defaultValue.get(0));
			slider.setHighValue((Double) defaultValue.get(1));
			slider.setBlockIncrement(0.1);
			if (isActingLikeInteger) {
				lowValueLabel.setText(Integer.toString(defaultValue.get(0).intValue()));
				highValueLabel.setText(Integer.toString(defaultValue.get(1).intValue()));
			} else {
				DecimalFormat df = new DecimalFormat("0.00");
				lowValueLabel.setText(df.format((Double) defaultValue.get(0)));
				highValueLabel.setText(df.format((Double) defaultValue.get(1)));
			}
		} else if (genericTypeClass.equals(Integer.TYPE)) {
			double majorTickUnit = (minMaxPair.get(1).doubleValue() - minMaxPair.get(0).doubleValue()) / 4.0;
			majorTickUnit = Math.max(1, Math.floor(majorTickUnit)); //unit must be > 0
			slider.setMajorTickUnit(majorTickUnit);
			slider.setMin(minMaxPair.get(0).doubleValue());
			slider.setMax(minMaxPair.get(1).doubleValue());
			slider.setLowValue(defaultValue.get(0).doubleValue());
			slider.setHighValue(defaultValue.get(1).doubleValue());
			slider.setBlockIncrement(1);
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
		//TO DO: set the labes such that date fits
	}
	
	public boolean getTimeframe() {
		return this.isTimeframe;
	}
	
	public void setTimes(List<String> times) {
		this.times = times;
	}
}
