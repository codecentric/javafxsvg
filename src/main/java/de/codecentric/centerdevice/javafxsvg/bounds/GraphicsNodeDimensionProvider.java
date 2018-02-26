package de.codecentric.centerdevice.javafxsvg.bounds;

import java.awt.geom.Rectangle2D;

import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.DocumentLoader;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgent;
import org.apache.batik.bridge.UserAgentAdapter;
import org.w3c.dom.Document;

public class GraphicsNodeDimensionProvider implements DimensionProvider {

	@Override
	public Dimension getDimension(Document document) {
		UserAgent agent = new UserAgentAdapter();
		DocumentLoader loader = new DocumentLoader(agent);

		BridgeContext context = new BridgeContext(agent, loader);
		context.setDynamic(true);

		GVTBuilder builder = new GVTBuilder();
		Rectangle2D primitiveBounds = builder.build(context, document).getPrimitiveBounds();
		return new Dimension((float)primitiveBounds.getWidth(), (float)primitiveBounds.getHeight());
	}
}
