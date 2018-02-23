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
import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.DocumentLoader;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgent;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.util.XMLResourceDescriptor;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;

import static org.apache.batik.transcoder.SVGAbstractTranscoder.KEY_HEIGHT;
import static org.apache.batik.transcoder.SVGAbstractTranscoder.KEY_WIDTH;

public class SvgImageLoader extends ImageLoaderImpl {

	private static final int BYTES_PER_PIXEL = 4; // RGBA
	private static final int DEFAULT_SIZE = 400;
	private static final Logger LOGGER = Logger.getLogger(SvgImageLoader.class.getName());

	private InputStream input;
	private float maxPixelScale = 0;

	private int svtWidth = DEFAULT_SIZE;
	private int svtHeight = DEFAULT_SIZE;

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

		String svgString = IOUtils.toString(input);

		this.input = new ByteArrayInputStream(svgString.getBytes());

		//  Extracting the SVG attributes...
		Pattern pattern = Pattern.compile("[^<>\\n]*<svg([^>]*)>", Pattern.DOTALL);
		Matcher matcher = pattern.matcher(svgString);
		boolean fallback = true;

		if ( matcher.find() ) {

			String group = matcher.group();

			if ( group != null && !group.trim().isEmpty() ) {
				try {
					if ( group.contains("width") && group.contains("height") ) {
						//  Extracting width and height...
						svtWidth = parsingIntFromSize("width", group);
						svtHeight = parsingIntFromSize("height", group);
						fallback = false;
					} else if ( group.contains("viewBox") ) {

						//  Extracting viewBox...
						int propertyIndex = group.indexOf("viewBox");
						int startIndex = group.indexOf('"', propertyIndex);
						int endIndex = group.indexOf('"', startIndex + 1);

						if ( startIndex >= 0 && endIndex >= 0 ) {

							String viewBoxString = group.substring(startIndex + 1, endIndex);
							String[] split = viewBoxString.split(viewBoxString.contains(",") ? "," : " ");

							if ( split.length >= 4 ) {
								svtWidth = (int) Math.round(Double.valueOf(split[2]));
								svtHeight = (int) Math.round(Double.valueOf(split[3]));
								fallback = false;
							}

						}

					}
				} catch ( NumberFormatException ex ) {
					LOGGER.log(
						Level.WARNING,
						"Errors determining image size from SVG attributes: {0} – {1}\n{2}",
						new String[] { ex.getClass().getName(), ex.getMessage(), group }
					);
				}
			}

		}

		if ( fallback ) {
			fallbackSizeRetrieval(svgString);
		}

	}

	private int parsingIntFromSize( String name, String group ) {

		int propertyIndex = group.indexOf(name);
		int startIndex = group.indexOf('"', propertyIndex);
		int endIndex = group.indexOf('"', startIndex + 1);

		if ( startIndex >= 0 && endIndex >= 0 ) {
			return Integer.parseInt(group.substring(startIndex + 1, endIndex));
		} else {
			throw new IllegalStateException(MessageFormat.format("No value for \"{0}\" attribute", name));
		}

	}

	private void fallbackSizeRetrieval( String svgString ) {

		try {

			SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(XMLResourceDescriptor.getXMLParserClassName());
			InputStream is = new ByteArrayInputStream(svgString.getBytes());
			Document document = factory.createDocument(null, is);
			UserAgent agent = new UserAgentAdapter();
			DocumentLoader loader = new DocumentLoader(agent);
			BridgeContext context = new BridgeContext(agent, loader);

			context.setDynamic(true);

			GVTBuilder builder = new GVTBuilder();
			GraphicsNode root = builder.build(context, document);

			svtWidth = (int) root.getPrimitiveBounds().getWidth();
			svtHeight = (int) root.getPrimitiveBounds().getHeight();

		} catch ( IOException ex ) {
			LOGGER.log(
				Level.WARNING,
				"Errors determining image size from SVG using fallback implementation: {0} – {1}",
				new String[] { ex.getClass().getName(), ex.getMessage() }
			);
		}

	}

	/** Package visibility for unit test. */
	int getSvtHeight() {
		return svtHeight;
	}

	/** Package visibility for unit test. */
	int getSvtWidth() {
		return svtWidth;
	}

	@Override
	public ImageFrame load( int imageIndex, int width, int height, boolean preserveAspectRatio, boolean smooth )
		throws IOException {

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

	private BufferedImage getTranscodedImage( float width, float height ) throws TranscoderException {
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
