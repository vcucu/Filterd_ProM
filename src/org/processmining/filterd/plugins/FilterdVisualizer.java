package org.processmining.filterd.plugins;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.annotations.Visualizer;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginLevel;

public class FilterdVisualizer extends JPanel {

	
	private static final long serialVersionUID = 9104736085046064684L;
	private JPanel Main;
	private JPanel FilterPanel;
	private JComboBox DatasetDropdown;
	private JButton AddFilter;
	private JToggleButton Toggle;
	private JScrollPane ScrollPane;
	private JList FilterList;
	private JButton RemoveFilter;
	private JLabel DatasetTitle;
	private JLabel FiltersTitle;
	private JPanel VisualizerPanel;
	private JComboBox VisualizerDropdown;

	@Plugin(name = "Filterd Visualizer", level = PluginLevel.PeerReviewed, parameterLabels = "Filter",
			returnTypes = JComponent.class, returnLabels = "Filterd Notebook Visualizer", userAccessible = true,
			mostSignificantResult = 1, help = "Help text goes here")
	@Visualizer(name = "Filterd Visualizer", pack = "Filterd")
	public JComponent visualize(final PluginContext context, final XLog log) {
		initComponents();
		return Main;
	}
	
	private void initComponents() {
		Main = new JPanel();
		FilterPanel = new JPanel();
		DatasetDropdown = new JComboBox();
		AddFilter = new JButton();
		Toggle = new JToggleButton();
		ScrollPane = new JScrollPane();
		FilterList = new JList();
		RemoveFilter = new JButton();
		DatasetTitle = new JLabel();
		FiltersTitle = new JLabel();
		VisualizerPanel = new JPanel();
		VisualizerDropdown = new JComboBox();

		//======== Main ========
		{
			//======== FilterPanel ========
			{
				FilterPanel.setBorder(new EtchedBorder());

				//---- DatasetDropdown ----
				DatasetDropdown.addItemListener(e -> DatasetDropdownItemStateChanged(e));

				//---- AddFilter ----
				AddFilter.setText("Add Filter");
				AddFilter.addActionListener(e -> AddFilterActionPerformed(e));

				//---- Toggle ----
				Toggle.setText("Toggle");
				Toggle.addActionListener(e -> ToggleActionPerformed(e));

				//======== ScrollPane ========
				{
					ScrollPane.setViewportView(FilterList);
				}

				//---- RemoveFilter ----
				RemoveFilter.setText("Remove Filter");
				RemoveFilter.addActionListener(e -> RemoveFilterActionPerformed(e));

				//---- DatasetTitle ----
				DatasetTitle.setText("Dataset");
				DatasetTitle.setHorizontalAlignment(SwingConstants.CENTER);

				//---- FiltersTitle ----
				FiltersTitle.setText("Filters");
				FiltersTitle.setHorizontalAlignment(SwingConstants.CENTER);

				GroupLayout FilterPanelLayout = new GroupLayout(FilterPanel);
				FilterPanel.setLayout(FilterPanelLayout);
				FilterPanelLayout.setHorizontalGroup(
					FilterPanelLayout.createParallelGroup()
						.addGroup(FilterPanelLayout.createSequentialGroup()
							.addContainerGap()
							.addGroup(FilterPanelLayout.createParallelGroup()
								.addComponent(DatasetDropdown, GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
								.addComponent(Toggle, GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
								.addComponent(ScrollPane, GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
								.addComponent(AddFilter, GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
								.addComponent(RemoveFilter, GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
								.addComponent(DatasetTitle, GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
								.addComponent(FiltersTitle, GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE))
							.addContainerGap())
				);
				FilterPanelLayout.setVerticalGroup(
					FilterPanelLayout.createParallelGroup()
						.addGroup(FilterPanelLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(DatasetTitle)
							.addGap(9, 9, 9)
							.addComponent(DatasetDropdown, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(18, 18, 18)
							.addComponent(FiltersTitle)
							.addGap(9, 9, 9)
							.addComponent(ScrollPane, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
							.addGap(18, 18, 18)
							.addComponent(AddFilter)
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(RemoveFilter)
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 519, Short.MAX_VALUE)
							.addComponent(Toggle)
							.addContainerGap())
				);
			}

			//======== VisualizerPanel ========
			{
				VisualizerPanel.setBorder(new EtchedBorder());

				//---- VisualizerDropdown ----
				VisualizerDropdown.addItemListener(e -> VisualizerDropdownItemStateChanged(e));

				GroupLayout VisualizerPanelLayout = new GroupLayout(VisualizerPanel);
				VisualizerPanel.setLayout(VisualizerPanelLayout);
				VisualizerPanelLayout.setHorizontalGroup(
					VisualizerPanelLayout.createParallelGroup()
						.addGroup(GroupLayout.Alignment.TRAILING, VisualizerPanelLayout.createSequentialGroup()
							.addContainerGap(763, Short.MAX_VALUE)
							.addComponent(VisualizerDropdown, GroupLayout.PREFERRED_SIZE, 175, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())
				);
				VisualizerPanelLayout.setVerticalGroup(
					VisualizerPanelLayout.createParallelGroup()
						.addGroup(VisualizerPanelLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(VisualizerDropdown, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addContainerGap(760, Short.MAX_VALUE))
				);
			}

			GroupLayout MainLayout = new GroupLayout(Main);
			Main.setLayout(MainLayout);
			MainLayout.setHorizontalGroup(
				MainLayout.createParallelGroup()
					.addGroup(MainLayout.createSequentialGroup()
						.addContainerGap()
						.addComponent(FilterPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGap(18, 18, 18)
						.addComponent(VisualizerPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addContainerGap())
			);
			MainLayout.setVerticalGroup(
				MainLayout.createParallelGroup()
					.addGroup(MainLayout.createSequentialGroup()
						.addContainerGap()
						.addGroup(MainLayout.createParallelGroup()
							.addComponent(VisualizerPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(FilterPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addContainerGap())
			);
		}
	}

	private void DatasetDropdownItemStateChanged(ItemEvent e) {
		// TODO add your code here
	}

	private void AddFilterActionPerformed(ActionEvent e) {
		// TODO add your code here
	}

	private void ToggleActionPerformed(ActionEvent e) {
		// TODO add your code here
	}

	private void RemoveFilterActionPerformed(ActionEvent e) {
		// TODO add your code here
	}

	private void VisualizerDropdownItemStateChanged(ItemEvent e) {
		// TODO add your code here
	}
}
