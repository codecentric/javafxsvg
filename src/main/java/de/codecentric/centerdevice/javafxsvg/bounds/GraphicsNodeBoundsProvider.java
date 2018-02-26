package de.codecentric.centerdevice.javafxsvg.bounds;

import java.awt.geom.Rectangle2D;

import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.DocumentLoader;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgent;
import org.apache.batik.bridge.UserAgentAdapter;
import org.w3c.dom.Document;

public class GraphicsNodeBoundsProvider implements BoundsProvider {

	@Override
	public Rectangle2D getBounds(Document document) {
		UserAgent agent = new UserAgentAdapter();
		DocumentLoader loader = new DocumentLoader(agent);

		BridgeContext context = new BridgeContext(agent, loader);
		context.setDynamic(true);

		GVTBuilder builder = new GVTBuilder();
		return builder.build(context, document).getPrimitiveBounds();
	}
}
