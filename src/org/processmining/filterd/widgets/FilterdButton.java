package org.processmining.filterd.widgets;

import java.awt.Color;

import com.fluxicon.slickerbox.components.SlickerButton;

public class FilterdButton extends SlickerButton {

	private static final long serialVersionUID = 9114976527558417296L;

	public FilterdButton(String title) {
		super(title);
		this.COLOR_BG = new Color(0.0f, 1.0f, 0.0f);
		this.COLOR_BG_ACTIVE = new Color(0.0f, 1.0f, 0.0f);
		this.COLOR_BG_DISABLED = new Color(0.0f, 1.0f, 0.0f);
		this.COLOR_BG_FOCUS = new Color(0.0f, 1.0f, 0.0f);
		this.COLOR_BG_MOUSEOVER = new Color(0.0f, 1.0f, 0.0f);
		
		this.COLOR_HILIGHT = new Color(1.0f, 0.0f, 0.0f);
		this.COLOR_HILIGHT_ACTIVE = new Color(1.0f, 0.0f, 0.0f);
		this.COLOR_HILIGHT_DISABLED = new Color(1.0f, 0.0f, 0.0f);
		this.COLOR_HILIGHT_FOCUS = new Color(1.0f, 0.0f, 0.0f);
		this.COLOR_HILIGHT_MOUSEOVER = new Color(1.0f, 0.0f, 0.0f);
		
		this.COLOR_TEXT = new Color(0.0f, 0.0f, 1.0f);
		this.COLOR_TEXT_ACTIVE = new Color(0.0f, 0.0f, 1.0f);
		this.COLOR_TEXT_DISABLED = new Color(0.0f, 0.0f, 1.0f);
		this.COLOR_TEXT_FOCUS = new Color(0.0f, 0.0f, 1.0f);
		this.COLOR_TEXT_MOUSEOVER = new Color(0.0f, 0.0f, 1.0f);
		
		this.disabledBgBuffer = null;
		this.focusBgBuffer = null;
		this.passiveBgBuffer = null;
		this.shinyImage = null;
	}
}
