package de.codecentric.centerdevice.javafxsvg;

import java.io.IOException;
import java.io.InputStream;

import com.sun.javafx.iio.ImageFormatDescription;
import com.sun.javafx.iio.ImageLoader;
import com.sun.javafx.iio.ImageLoaderFactory;
import com.sun.javafx.iio.ImageStorage;

import de.codecentric.centerdevice.javafxsvg.bounds.BoundsProvider;
import de.codecentric.centerdevice.javafxsvg.bounds.DefaultBoundsProvider;

public class SvgImageLoaderFactory implements ImageLoaderFactory {
	private static final SvgImageLoaderFactory instance = new SvgImageLoaderFactory();

	private static BoundsProvider boundsProvider;

	public static final void install() {
		install(new DefaultBoundsProvider());
	}

	public static final void install(BoundsProvider boundsProvider) {
		SvgImageLoaderFactory.boundsProvider = boundsProvider;
		
		ImageStorage.addImageLoaderFactory(instance);
	}

	public static final ImageLoaderFactory getInstance() {
		return instance;
	}

	@Override
	public ImageFormatDescription getFormatDescription() {
		return SvgDescriptor.getInstance();
	}

	@Override
	public ImageLoader createImageLoader(InputStream input) throws IOException {
		return new SvgImageLoader(input, boundsProvider);
	}
}
