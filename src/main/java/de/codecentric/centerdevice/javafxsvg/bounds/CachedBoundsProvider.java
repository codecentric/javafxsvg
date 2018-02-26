package de.codecentric.centerdevice.javafxsvg.bounds;

import java.awt.geom.Rectangle2D;

public class CachedBoundsProvider implements BoundsProvider {

	private final BoundsProvider source;
	private Rectangle2D bounds;

	CachedBoundsProvider(BoundsProvider source) {
		this.source = source;
	}

	@Override
	public Rectangle2D getPrimitiveBounds() {
		if (bounds == null) {
			bounds = source.getPrimitiveBounds();
		}
		return bounds;
	}

}
