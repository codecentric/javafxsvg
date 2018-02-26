package de.codecentric.centerdevice.javafxsvg;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;


/**
 * @author claudio.rosati@esss.se
 */
@SuppressWarnings( "ClassWithoutLogger" )
public class SvgImageLoaderTest {

	@Test()
	@SuppressWarnings( "UseOfSystemOutOrSystemErr" )
	public void testUpdateSVTSizeBacon() {

		System.out.println("updateSVTSize - 'bacon.svg' stream");

		InputStream input = this.getClass().getClassLoader().getResourceAsStream("bacon.svg");
		SvgImageLoader loader = new SvgImageLoader(input);
		int[] result = new int[] { loader.getSvtWidth(), loader.getSvtHeight() };
		int[] expResult = new int[] { 64, 29 };

		assertArrayEquals(expResult, result);

	}

	@Test()
	@SuppressWarnings( "UseOfSystemOutOrSystemErr" )
	public void testUpdateSVTSizeBaconCommented() {

		System.out.println("updateSVTSize - 'bacon-commented.svg' stream");

		InputStream input = this.getClass().getClassLoader().getResourceAsStream("bacon-commented.svg");
		SvgImageLoader loader = new SvgImageLoader(input);
		int[] result = new int[] { loader.getSvtWidth(), loader.getSvtHeight() };
		int[] expResult = new int[] { 64, 29 };

		assertArrayEquals(expResult, result);

	}

	@Test()
	@SuppressWarnings( "UseOfSystemOutOrSystemErr" )
	public void testUpdateSVTSizeClose() {

		System.out.println("updateSVTSize - 'close.svg' stream");

		InputStream input = this.getClass().getClassLoader().getResourceAsStream("close.svg");
		SvgImageLoader loader = new SvgImageLoader(input);
		int[] result = new int[] { loader.getSvtWidth(), loader.getSvtHeight() };
		int[] expResult = new int[] { 12, 12 };

		assertArrayEquals(expResult, result);

	}

	@Test()
	@SuppressWarnings( "UseOfSystemOutOrSystemErr" )
	public void testUpdateSVTSizeCloseCommented() {

		System.out.println("updateSVTSize - 'close-commented.svg' stream");

		InputStream input = this.getClass().getClassLoader().getResourceAsStream("close-commented.svg");
		SvgImageLoader loader = new SvgImageLoader(input);
		int[] result = new int[] { loader.getSvtWidth(), loader.getSvtHeight() };
		int[] expResult = new int[] { 12, 12 };

		assertArrayEquals(expResult, result);

	}

	@Test()
	@SuppressWarnings( "UseOfSystemOutOrSystemErr" )
	public void testUpdateSVTSizeEmptyStream() {

		System.out.println("updateSVTSize - empty stream");

		InputStream input = new ByteArrayInputStream(new byte[] {});
		SvgImageLoader loader = new SvgImageLoader(input);
		int[] result = new int[] { loader.getSvtWidth(), loader.getSvtHeight() };
		int[] expResult = new int[] { 400, 400 };

		assertArrayEquals(expResult, result);

	}

	@Test()
	@SuppressWarnings( "UseOfSystemOutOrSystemErr" )
	public void testUpdateSVTSizeFallbackBacon() {

		System.out.println("updateSVTSize - 'fallback-bacon.svg' stream");

		InputStream input = this.getClass().getClassLoader().getResourceAsStream("fallback-bacon.svg");
		SvgImageLoader loader = new SvgImageLoader(input);
		int[] result = new int[] { loader.getSvtWidth(), loader.getSvtHeight() };
		int[] expResult = new int[] { 64, 29 };

		assertArrayEquals(expResult, result);

	}

	@Test()
	@SuppressWarnings( "UseOfSystemOutOrSystemErr" )
	public void testUpdateSVTSizeFallbackStop() {

		System.out.println("updateSVTSize - 'fallback-stop.svg' stream");

		InputStream input = this.getClass().getClassLoader().getResourceAsStream("fallback-stop.svg");
		SvgImageLoader loader = new SvgImageLoader(input);
		int[] result = new int[] { loader.getSvtWidth(), loader.getSvtHeight() };
		int[] expResult = new int[] { 1000, 1000 };

		assertArrayEquals(expResult, result);

	}

	@Test()
	@SuppressWarnings( "UseOfSystemOutOrSystemErr" )
	public void testUpdateSVTSizePumpError() {

		System.out.println("updateSVTSize - 'pump_error.svg' stream");

		InputStream input = this.getClass().getClassLoader().getResourceAsStream("pump_error.svg");
		SvgImageLoader loader = new SvgImageLoader(input);
		int[] result = new int[] { loader.getSvtWidth(), loader.getSvtHeight() };
		int[] expResult = new int[] { 60, 36 };

		assertArrayEquals(expResult, result);

	}

	@Test()
	@SuppressWarnings( "UseOfSystemOutOrSystemErr" )
	public void testUpdateSVTSizeStop() {

		System.out.println("updateSVTSize - 'stop.svg' stream");

		InputStream input = this.getClass().getClassLoader().getResourceAsStream("stop.svg");
		SvgImageLoader loader = new SvgImageLoader(input);
		int[] result = new int[] { loader.getSvtWidth(), loader.getSvtHeight() };
		int[] expResult = new int[] { 1000, 1000 };

		assertArrayEquals(expResult, result);

	}

}
