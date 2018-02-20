package de.codecentric.centerdevice.javafxsvg;

import com.sun.javafx.iio.ImageFrame;
import com.sun.javafx.iio.ImageStorage;
import com.sun.javafx.iio.common.ImageLoaderImpl;
import com.sun.javafx.iio.common.ImageTools;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.stage.Screen;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.commons.io.IOUtils;

import static org.apache.batik.transcoder.SVGAbstractTranscoder.KEY_HEIGHT;
import static org.apache.batik.transcoder.SVGAbstractTranscoder.KEY_WIDTH;

public class SvgImageLoader extends ImageLoaderImpl {

	private static final int BYTES_PER_PIXEL = 4; // RGBA
	private static final int DEFAULT_SIZE = 400;
    private static final Logger LOGGER = Logger.getLogger(SvgImageLoader.class.getName());

	private InputStream input;
	private float maxPixelScale = 0;

    /** Package visibility for unit test. */
    @SuppressWarnings( "PackageVisibleField" )
    int svtWidth = DEFAULT_SIZE;
    
    /** Package visibility for unit test. */
    @SuppressWarnings( "PackageVisibleField" )
    int svtHeight = DEFAULT_SIZE;

    protected SvgImageLoader( InputStream input ) {

        super(SvgDescriptor.getInstance());

        if ( input == null ) {
            throw new IllegalArgumentException("input == null!");
        }

        try {
            updateSVTSize(input);
        } catch ( IOException ex ) {
            LOGGER.log(Level.WARNING, "SVG size cannot be determined.", ex);
        }

    }

    private void updateSVTSize( InputStream input ) throws IOException {

        LOGGER.info(MessageFormat.format("**** input class: {0}", input.getClass().getName()));

        String svgString = IOUtils.toString(input);

        this.input = new ByteArrayInputStream(svgString.getBytes());

        //  Extracting the SVG attributes...
        Pattern pattern = Pattern.compile("[^<>\\n]*<svg([^>]*)>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(svgString);

        if ( matcher.find() ) {

            String group = matcher.group();

            if ( group != null && !group.trim().isEmpty() ) {
                if ( group.contains("width") && group.contains("height") ) {

                    //  Extracting width...
                    int propertyIndex = group.indexOf("width");
                    int startIndex = group.indexOf('"', propertyIndex);
                    int endIndex = group.indexOf('"', startIndex + 1);

                    if ( startIndex >= 0 && endIndex >= 0 ) {

                        String widthString = group.substring(startIndex + 1, endIndex);

                        try {
                            svtWidth = Integer.parseInt(widthString);
                        } catch ( NumberFormatException nfex ) {
                            LOGGER.log(Level.WARNING, "SVG 'width' is not a number: {0}", widthString);
                        }

                    }

                    //  Extracting height...
                    propertyIndex = group.indexOf("height");
                    startIndex = group.indexOf('"', propertyIndex);
                    endIndex = group.indexOf('"', startIndex + 1);

                    if ( startIndex >= 0 && endIndex >= 0 ) {

                        String heightString = group.substring(startIndex + 1, endIndex);

                        try {
                            svtHeight = Integer.parseInt(heightString);
                        } catch ( NumberFormatException nfex ) {
                            LOGGER.log(Level.WARNING, "SVG 'height' is not a number: {0}", heightString);
                        }

                    }

                } else if ( group.contains("viewBox") ) {

                    //  Extracting viewBox...
                    int propertyIndex = group.indexOf("viewBox");
                    int startIndex = group.indexOf('"', propertyIndex);
                    int endIndex = group.indexOf('"', startIndex + 1);

                    if ( startIndex >= 0 && endIndex >= 0 ) {

                        String viewBoxString = group.substring(startIndex + 1, endIndex);
                        String[] split = viewBoxString.split(viewBoxString.contains(",") ? "," : " ");

                        if ( split.length >= 4 ) {

                            try {
                                svtWidth = (int) Math.round(Double.valueOf(split[2]));
                            } catch ( NumberFormatException nfex ) {
                                LOGGER.log(Level.WARNING, "SVG 'viewBox' doesn't contain a valid width: {0}", viewBoxString);
                            }

                            try {
                                svtHeight = (int) Math.round(Double.valueOf(split[3]));
                            } catch ( NumberFormatException nfex ) {
                                LOGGER.log(Level.WARNING, "SVG 'viewBox' doesn't contain a valid height: {0}", viewBoxString);
                            }

                        }

                    }

                }

            }

        }

    }

    @Override
    public ImageFrame load( int imageIndex, int width, int height, boolean preserveAspectRatio, boolean smooth )
        throws IOException
    {
    
        if ( 0 != imageIndex ) {
            return null;
        }

        int[] widthHeight = ImageTools.computeDimensions(svtWidth, svtHeight, width, height, preserveAspectRatio);
        int imageWidth = widthHeight[0];
        int imageHeight = widthHeight[1];

        try {
            return createImageFrame(imageWidth, imageHeight, getPixelScale());
        } catch ( TranscoderException ex ) {
            throw new IOException(ex);
        } finally {
            //  Release resources.
            input = null;
        }

    }

	public float getPixelScale() {
		if (maxPixelScale == 0) {
			maxPixelScale = calculateMaxRenderScale();
		}
		return maxPixelScale;
	}

	public float calculateMaxRenderScale() {
		float maxRenderScale = 0;
		ScreenHelper.ScreenAccessor accessor = ScreenHelper.getScreenAccessor();
		for (Screen screen : Screen.getScreens()) {
			maxRenderScale = Math.max(maxRenderScale, accessor.getRenderScale(screen));
		}
		return maxRenderScale;
	}

	private ImageFrame createImageFrame(int width, int height, float pixelScale) throws TranscoderException {
		BufferedImage bufferedImage = getTranscodedImage(width * pixelScale, height * pixelScale);
		ByteBuffer imageData = getImageData(bufferedImage);

		return new FixedPixelDensityImageFrame(ImageStorage.ImageType.RGBA, imageData, bufferedImage.getWidth(),
				bufferedImage.getHeight(), getStride(bufferedImage), null, pixelScale, null);
	}

    private BufferedImage getTranscodedImage(float width, float height) throws TranscoderException {
		BufferedImageTranscoder trans = new BufferedImageTranscoder(BufferedImage.TYPE_INT_ARGB);
		trans.addTranscodingHint(KEY_WIDTH, width);
		trans.addTranscodingHint(KEY_HEIGHT, height);
		trans.transcode(new TranscoderInput(this.input), null);

		return trans.getBufferedImage();
	}

	private int getStride(BufferedImage bufferedImage) {
		return bufferedImage.getWidth() * BYTES_PER_PIXEL;
	}

	private ByteBuffer getImageData(BufferedImage bufferedImage) {
		int[] rgb = bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null, 0,
				bufferedImage.getWidth());

		byte[] imageData = new byte[getStride(bufferedImage) * bufferedImage.getHeight()];

		copyColorToBytes(rgb, imageData);
		return ByteBuffer.wrap(imageData);
	}

	private void copyColorToBytes(int[] rgb, byte[] imageData) {
		if (rgb.length * BYTES_PER_PIXEL != imageData.length) {
			throw new ArrayIndexOutOfBoundsException();
		}

		ByteBuffer byteBuffer = ByteBuffer.allocate(Integer.BYTES);

		for (int i = 0; i < rgb.length; i++) {
			byte[] bytes = byteBuffer.putInt(rgb[i]).array();

			int dataOffset = BYTES_PER_PIXEL * i;
			imageData[dataOffset] = bytes[1];
			imageData[dataOffset + 1] = bytes[2];
			imageData[dataOffset + 2] = bytes[3];
			imageData[dataOffset + 3] = bytes[0];

			byteBuffer.clear();
		}
	}

	@Override
	public void dispose() {
		// Nothing to do
	}
}
