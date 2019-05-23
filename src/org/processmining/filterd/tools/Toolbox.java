package org.processmining.filterd.tools;

import java.time.LocalDateTime;

import org.deckfour.xes.model.XAttribute;

public class Toolbox {

	private static final Toolbox INSTANCE = new Toolbox();

	private Toolbox() {}

	public static Toolbox getInstance() {
		return INSTANCE;
	}

	public String getType(XAttribute attribute) {
		String type = attribute.getClass().getSimpleName();
		
		switch(type) {
			case "XAttributeLiteralImpl":
				return "Literal";
			case "XAttributeBooleanImpl":
				return "Boolean";
			case "XAttributeContinuousImpl":
				return "Continuous";
			case "XAttributeDiscreteImpl":
				return "Discrete";
			case "XAttributeIDImpl":
				return "ID";
			case "XAttributeTimestampImpl":
				return "Timestamp";
			default: return "Other";
		}
	}
	
	/* time format assumed to be YYYY-MM-DDThh:mm:ss.SSSZ */
	public LocalDateTime synchronizeGMT(String time) {
		LocalDateTime date = LocalDateTime.parse(time.substring(0, 23));
		int offsetH;
		int offsetM;
		
		if (time.length() > 23) {
			boolean sign = false;
			if (time.charAt(23) == '+') sign = true;
			
			offsetH = Integer.parseInt(time.substring(24, 26));
			offsetM = Integer.parseInt(time.substring(27, 29));
			
			if (sign) {
				return date.plusHours(offsetH).plusMinutes(offsetM);
			} else {
				return date.minusHours(offsetH).minusMinutes(offsetM);
			}
		}

		return date;
	}
}
