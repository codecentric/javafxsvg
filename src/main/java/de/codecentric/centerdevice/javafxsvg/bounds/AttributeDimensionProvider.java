package de.codecentric.centerdevice.javafxsvg.bounds;

import org.w3c.dom.Document;

public class AttributeDimensionProvider implements DimensionProvider {

	private static final String HEIGHT = "height";
	private static final String WIDTH = "width";
	
	@Override
	public Dimension getDimension(Document document) {
		return new Dimension(getFloatAttribute(document, WIDTH), getFloatAttribute(document, HEIGHT));
	}

	private float getFloatAttribute(Document document, String name) {
		try {
			return Float.parseFloat(document.getDocumentElement().getAttribute(name));
		} catch (NumberFormatException e) {
			return 0;
		}
	}
}

