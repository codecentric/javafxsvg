package de.codecentric.centerdevice.javafxsvg;

import java.nio.ByteBuffer;

import com.sun.javafx.iio.ImageFrame;
import com.sun.javafx.iio.ImageMetadata;
import com.sun.javafx.iio.ImageStorage.ImageType;

public class FixedPixelDensityImageFrame extends ImageFrame {

	public FixedPixelDensityImageFrame(ImageType imageType, ByteBuffer imageData, int width, int height, int stride,
			byte[][] palette, float pixelScale, ImageMetadata metadata) {
		super(imageType, imageData, width, height, stride, palette, pixelScale, metadata);
	}

	@Override
	public void setPixelScale(float pixelScale) {
		// Prevent ImageStorage class from overwriting the pixel density
	}
}
