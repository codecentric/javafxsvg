package de.codecentric.centerdevice.javafxsvg.bounds;

import java.awt.geom.Rectangle2D;

import org.w3c.dom.Document;

public interface BoundsProvider {
	Rectangle2D getBounds(Document document);
}
