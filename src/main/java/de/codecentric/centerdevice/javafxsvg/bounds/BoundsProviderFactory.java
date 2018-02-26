package de.codecentric.centerdevice.javafxsvg.bounds;

import org.w3c.dom.Document;

public class BoundsProviderFactory {
	
	public static BoundsProvider fromDocument(Document document) {
		return new CachedBoundsProvider(new GraphicsNodeBoundsProvider(document));
	}
}
