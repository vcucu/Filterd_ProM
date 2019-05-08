package timeframe;

import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.deckfour.xes.info.impl.XTimeBoundsImpl;
import org.deckfour.xes.model.XAttribute;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.util.ui.widgets.ProMPropertiesPanel;
import org.processmining.framework.util.ui.widgets.ProMTextField;

import Widgets.RangeSlider;
import info.clearthought.layout.TableLayout;

public class TimeframePanel extends JPanel {
	private static final long serialVersionUID = -8597125322091926802L;
	
	private XTimeBoundsImpl bounds; 
	private JCheckBox removeEmptyTracesComponent;
	TimeframeParameters parameters;
	ProMPropertiesPanel panel;
	ProMTextField nameLabel;
	RangeSlider slider;
	ArrayList<XAttribute> times;
	
	public TimeframePanel(PluginContext context, TimeframeParameters parameters) {
		this.times = parameters.getTimes();
		double size[][] = {{700}, {100, 30, 500}};
		setLayout(new TableLayout(size));
		add(new ProMPropertiesPanel("Trim to timeframe"), "0, 0");
		RangeSlider slider = new RangeSlider(0, times.size() - 1, 0, times.size() - 1, 1);
		parameters.setLower(times.get(0).toString());
		parameters.setUpper(times.get(times.size() - 1).toString());
		
		slider.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
				parameters.setLower(times.get(slider.getLowValue()).toString());
				parameters.setUpper(times.get(slider.getHighValue()).toString());
			}
		});
		
		add(slider, "0, 1");
		this.panel = new ProMPropertiesPanel("");
		
		String newName = parameters.getName() + " (filter timeframe)";
		nameLabel = panel.addTextField("Log name", newName);
		
		// removeEmptyTracesComponent = addCheckBox("Remove trace if all events are removed", parameters.isRemoveEmptyTraces());		
		removeEmptyTracesComponent = panel.addCheckBox("Remove empty traces", parameters.isRemoveEmptyTraces());
		
		add(panel, "0, 2");
	}

	public ProMTextField getNameLabel() {
		return nameLabel;
	}

	public JCheckBox getRemoveEmptyTracesComponent() {
		return removeEmptyTracesComponent;
	}

	public XAttribute getLowerTime() {
		return times.get(slider.getLowValue());
	}

	public XAttribute getUpperTime() {
		return times.get(slider.getHighValue());
	}
}
