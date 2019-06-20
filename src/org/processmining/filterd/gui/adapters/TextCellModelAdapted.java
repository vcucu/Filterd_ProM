package org.processmining.filterd.gui.adapters;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class representing a deserialized text cell model. It is used by JAXB to save
 * the text cell model in XML format. All attributes of this class have to be
 * either primitives or enumerations.
 */
@XmlRootElement(name = "Text Cell")
public class TextCellModelAdapted extends CellModelAdapted {

	private String comment; // comment value of the text cell

	/**
	 * Getter for the comment value
	 * 
	 * @return comment value
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * Setter for the comment value
	 * 
	 * @param comment
	 *            comment value
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
}
