package org.processmining.filterd.gui.adapters;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TextCellModelAdapted extends CellModelAdapted {
	
	private String comment;

	public String getComment() {
		return comment;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
}
