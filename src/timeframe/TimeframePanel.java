package timeframe;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.deckfour.xes.info.impl.XTimeBoundsImpl;
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
	ArrayList<String> times;
	JTextField startDate;
	JTextField startTime;
	JTextField endDate;
	JTextField endTime;
	JPanel timeSettings;
	
	public TimeframePanel(PluginContext context, TimeframeParameters parameters) {
		this.times = parameters.getTimes();
		double size[][] = {{800}, {60, 30, 170, 40, 120}};
		setLayout(new TableLayout(size));
		add(new ProMPropertiesPanel("Trim to timeframe"), "0, 0");
		parameters.setLower(times.get(0));
		parameters.setUpper(times.get(times.size() - 1));
		

		RangeSlider slider = new RangeSlider(0, times.size() - 1, 0, times.size() - 1, 1);
		add(slider, "0, 1");
		
		/* slider listener */
		slider.addChangeListener(new ChangeListener(){
			
			public void stateChanged(ChangeEvent e) {
				validateTextFields();
				parameters.setLower(times.get(slider.getLowValue()));
				parameters.setUpper(times.get(slider.getHighValue()));
				
				/* update the displayed date and time */
				String[] start = splitTimestamp(times.get(slider.getLowValue()));
				startDate.setText(start[0]);
				startTime.setText(start[1]);
				String[] end = splitTimestamp(times.get(slider.getHighValue()));
				endDate.setText(end[0]);
				endTime.setText(end[1]);
				
				timeSettings.repaint();
			}
			
		});
		
		sliderInput();
		add(timeSettings, "0, 2");
		
		/* update values button */
		JPanel updatePanel = new JPanel();
		updatePanel.setLayout(new TableLayout(new double[][]{{315, 170, 315}, {30, 10}}));
		JButton updateButton = new JButton("Update timeframe");
		updateButton.setFocusPainted(false);
		updateButton.setBackground(new Color(140, 140, 140));
		updateButton.setForeground(Color.WHITE);
		updateButton.setFont(new Font("Arial", Font.BOLD, 14));
		updateButton.addActionListener(new ActionListener(){
			  public void actionPerformed(ActionEvent e) {
				  String sDate = startDate.getText();
				  String sTime = startTime.getText();
				  String eDate = endDate.getText();
				  String eTime = endTime.getText();
				  boolean check = true; 

				  validateTextFields();
				  /* if invalid input, make text field red */
				  if (!validDate(sDate)) {
					  check = false; 
					  startDate.setBorder(new LineBorder(Color.RED));
				  }
				  
				  if (!validTime(sTime)) {
					  check = false;
					  startTime.setBorder(new LineBorder(Color.RED));
				  }
				  
				  if (!validDate(eDate)) {
					  check = false;
					  endDate.setBorder(new LineBorder(Color.RED));
				  }
				  
				  if (!validTime(eTime)) {
					  check = false;
					  endTime.setBorder(new LineBorder(Color.RED));
				  }
				  
				  if (check) {  
					  String startTimestamp = sDate + "T" + sTime;
					  String endTimestamp =  eDate + "T" + eTime;
					  slider.setLowValue(times.indexOf(ceilTimestamp(startTimestamp)));
					  slider.setHighValue(times.indexOf(floorTimestamp(endTimestamp)));
				  }
			  }
		});
		updatePanel.add(updateButton, "1, 0");
		add(updatePanel, "0,3");

		/* panel for updating name log and other settings */
		this.panel = new ProMPropertiesPanel("");
		String newName = parameters.getName() + " (filter timeframe)";
		nameLabel = panel.addTextField("Log name", newName);
		removeEmptyTracesComponent = panel.addCheckBox("Remove empty traces", parameters.isRemoveEmptyTraces());
		
		add(panel, "0, 4");
	}

	public ProMTextField getNameLabel() {
		return nameLabel;
	}

	public JCheckBox getRemoveEmptyTracesComponent() {
		return removeEmptyTracesComponent;
	}

	public String getLowerTime() {
		return times.get(slider.getLowValue());
	}

	public String getUpperTime() {
		return times.get(slider.getHighValue());
	}
	
	private void validateTextFields() {
		startDate.setBorder(UIManager.getBorder("TextField.border"));
		startTime.setBorder(UIManager.getBorder("TextField.border"));
		endDate.setBorder(UIManager.getBorder("TextField.border"));
		endTime.setBorder(UIManager.getBorder("TextField.border"));
	}
	
	/* Method that initializes the panel for inputting a date. 
	 * 
	 */
	private void sliderInput() {
		timeSettings = new JPanel();
		timeSettings.setLayout(new TableLayout(new double[][]{{10, 175, 255, 175, 175}, {40, 40, 40, 40, 10}}));
		
		String[] start = splitTimestamp(times.get(0));
		startDate = new JTextField(start[0]);
		startTime = new JTextField(start[1]);
		String[] end = splitTimestamp(times.get(times.size() - 1));
		endDate = new JTextField(end[0]);
		endTime = new JTextField(end[1]);

		timeSettings.add(new JLabel("Date (yyyy-mm-dd):"), "1, 0");
		timeSettings.add(new JLabel("Date (yyyy-mm-dd):"), "4, 0");
		timeSettings.add(startDate, "1, 1");
		timeSettings.add(startTime, "1, 3");
		timeSettings.add(new JLabel("Time (hh:mm:ss):"), "1, 2");
		timeSettings.add(new JLabel("Time (hh:mm:ss):"), "4, 2");
		timeSettings.add(endDate, "4, 1");
		timeSettings.add(endTime, "4, 3");
	}
	
	/* Method that return the date and time separated as two strings.
	 * @param timestamp - format yyyy-mm-ddThh:mm:ss+GMT
	 * @output - two strings:
	 * 				date yyyy-mm-dd
	 * 				time hh:mm:ss
	 */
	private String[] splitTimestamp(String timestamp) {
		String[] result = timestamp.split("T");
		return new String[] {result[0], result[1].split("\\+")[0]};
	}
	
	private String ceilTimestamp(String time) {
		for (int i = 0; i < times.size() - 1; i++) {
			if (time.compareTo(times.get(i)) <= 0) {
				return times.get(i);
			} 
		}
		
		return times.get(times.size() - 1);
	}
	
	private String floorTimestamp(String time) {
		for (int i = times.size() - 1; i > 0; i--) {
			if (time.compareTo(times.get(i)) >= 0) {
				return times.get(i);
			} 
		}
		
		return times.get(0);
	}
	
	private boolean validDate(String date) {
		String[] splitDate = date.split("\\-");
		
		if (splitDate.length != 3) {
			return false;
		}
		
		/* check if year is yyyy and numeric */
		if (splitDate[0].length() != 4) {
			return false;
		}
		try {
			Integer.parseInt(splitDate[0]);
		}
		catch (Exception e){
			return false;
		}
		
		/* check if month is mm, between 1 and 12, and numeric */
		if (splitDate[1].length() != 2) {
			return false;
		}
		try {
			int month = Integer.parseInt(splitDate[1]);
			if (month <= 0 || month > 12) {
				return false;
			}
		}
		catch (Exception e){
			return false;
		}
		
		/* check if day is dd, between 1 and 31, and numeric */
		if (splitDate[2].length() != 2) {
			return false;
		}
		try {
			int day = Integer.parseInt(splitDate[2]);
			if (day <= 0 || day > 31) {
				return false;
			}
		}
		catch (Exception e){
			return false;
		}
		
		return true;
	}
	
	private boolean validTime(String time) {
		String[] timeSplit = time.split(":");
		
		if (timeSplit.length != 3) {
			return false;
		}
		
		/* check if hour is hh and numeric */
		if (timeSplit[0].length() != 2) {
			System.out.println("time " + time + " is invalid cuz length hour " + timeSplit[0].length());
			return false;
		}
		try {
			int hour = Integer.parseInt(timeSplit[0]);
			if (hour < 0 || hour > 23) {
				System.out.println("invalid hour");
				return false;
			}
		}
		catch (Exception e){
			System.out.println("caught exception for hour");
			return false;
		}
		
		/* check if minute is mm, between 1 and 12, and numeric */
		if (timeSplit[1].length() != 2) {
			return false;
		}
		try {
			int minutes = Integer.parseInt(timeSplit[1]);
			if (minutes < 0 || minutes > 59) {
				System.out.println("invalid minute");
				return false;
			}
		}
		catch (Exception e){
			System.out.println("caught exception for minutes");
			return false;
		}
		
		/* check if second is ss, between 1 and 31, and numeric */
		if (timeSplit[2].length() != 2) {
			return false;
		}
		try {
			int seconds = Integer.parseInt(timeSplit[2]);
			if (seconds < 0 || seconds > 59) {
				System.out.println("invalid second");
				return false;
			}
		}
		catch (Exception e){
			System.out.println("caught exception for seconds");
			return false;
		}
		
		return true;

	}
}
