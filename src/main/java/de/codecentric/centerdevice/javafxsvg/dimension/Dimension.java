package de.codecentric.centerdevice.javafxsvg.dimension;

public class Dimension {
	private final float width;
	private final float height;

	public Dimension(float width, float height) {
		this.width = width;
		this.height = height;
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
}
