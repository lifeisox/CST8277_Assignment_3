/* File Name: ColorConverter.java
 * Author Name: Thorben Janssen
 * Modified By: Byung Seon Kim
 * Date: 6 March 2017
 * Description: How to implement a JPA (Java Persistence API) Attribute Converter. 
 * 	The following example shows a Converter implementation that can be used to store 
 * 	a java.awt.Color object as a String with the format red|green|blue|alpha in the database. 
 * 	When reading the entity from the database, the String will be used to instantiate a new Color object.
 * ---------------------------------------------------------------------------------------------
 * Reference: [1] T. Janssen, "How to implement a JPA Attribute Converter," [Online]. Available: 
 * 	http://www.thoughts-on-java.org/jpa-21-how-to-implement-type-converter/. [Accessed 6 March 2017].
 */
package server;

import java.awt.Color;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * The class shows a Converter implementation that can be used to store 
 * 	a java.awt.Color object as a String with the format red|green|blue|alpha in the database. 
 * 	When reading the entity from the database, the String will be used to instantiate a new Color object.
 * @author Modified by Byung Seon Kim
 */
@Converter(autoApply = true)
public class ColorConverter implements AttributeConverter<Color, String> {
	private static final String SEPARATOR = "-";

	/**
	 * Convert Color object to a String with format red|green|blue|alpha
	 * @param color 	Color object to convert String
	 */
	@Override
	public String convertToDatabaseColumn(Color color) {
		StringBuilder sb = new StringBuilder();
		sb.append( color.getRed() )
			.append( SEPARATOR )
	     	.append( color.getGreen() )
	     	.append( SEPARATOR )
	     	.append( color.getBlue() )
	     	.append( SEPARATOR )
	     	.append( color.getAlpha() );
		return sb.toString();
	}

	/**
	 * Convert a String with format red|green|blue|alpha to a Color object
	 * @param colorString String object to convert Color object
	 */
	@Override
	public Color convertToEntityAttribute( String colorString ) {
		try {
			String[] rgb = colorString.split( SEPARATOR );
			return new Color( Integer.parseInt(rgb[0]), 
					Integer.parseInt(rgb[1]),
					Integer.parseInt(rgb[2]), 
					Integer.parseInt(rgb[3]) );
		} catch ( NumberFormatException ex ) {
			return Color.RED;
		}
	}
}
