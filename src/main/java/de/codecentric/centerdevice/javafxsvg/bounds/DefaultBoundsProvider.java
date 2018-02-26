package de.codecentric.centerdevice.javafxsvg.bounds;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;

import org.w3c.dom.Document;

public class DefaultBoundsProvider implements BoundsProvider{

	private static final int DEFAULT_SIZE = 400;
	private static final Float BOUNDS = new Rectangle2D.Float(0, 0, DEFAULT_SIZE, DEFAULT_SIZE);
	
	@Override
	public Rectangle2D getBounds(Document document) {
		return BOUNDS;
	}
}
