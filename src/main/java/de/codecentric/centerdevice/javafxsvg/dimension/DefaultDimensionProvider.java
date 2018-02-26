package de.codecentric.centerdevice.javafxsvg.dimension;

import org.w3c.dom.Document;

public class DefaultDimensionProvider implements DimensionProvider{

	private static final int DEFAULT_SIZE = 400;
	private static final Dimension BOUNDS = new Dimension(DEFAULT_SIZE, DEFAULT_SIZE);
	
	@Override
	public Dimension getDimension(Document document) {
		return BOUNDS;
	}
}
