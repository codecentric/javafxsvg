package de.codecentric.centerdevice.javafxsvg.bounds;

import java.awt.geom.Rectangle2D;

import org.w3c.dom.Document;

public class AttributeBoundsProvider implements BoundsProvider {

	private static final String HEIGHT = "height";
	private static final String WIDTH = "width";
	
	@Override
	public Rectangle2D getBounds(Document document) {
		return new Rectangle2D.Float(0, 0, getFloatAttribute(document, WIDTH), getFloatAttribute(document, HEIGHT));
	}

	private float getFloatAttribute(Document document, String name) {
		try {
			return Float.parseFloat(document.getDocumentElement().getAttribute(name));
		} catch (NumberFormatException e) {
			return 0;
		}
	}
}

