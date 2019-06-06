package org.processmining.filterd.gui;

import org.deckfour.uitopia.api.model.Author;
import org.deckfour.uitopia.api.model.Resource;
import org.deckfour.uitopia.api.model.ResourceType;
import org.deckfour.uitopia.api.model.View;
import org.deckfour.uitopia.api.model.ViewType;

public class Utilities {
	
	/**	Dummy view type for not showing any visualizer in the dropdown  */
	public static ViewType dummyViewType = new ViewType() {

		@Override
		public View createView(Resource arg0) throws IllegalArgumentException {
			return null;
		}

		@Override
		public Author getAuthor() {
			return null;
		}

		@Override
		public Class<?> getPrimaryType() {
			return null;
		}

		@Override
		public String getTypeName() {
			return null;
		}
		
		@Override
		public String toString() {
			return "None";
		}

		@Override
		public ResourceType getViewableType() {
			return null;
		}
		
	};
	
}
