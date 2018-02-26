# JavaFxSVG

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/7f395ba27b374f9f9d5ad093f63d9682)](https://www.codacy.com/app/0x4a616e/javafxsvg?utm_source=github.com&utm_medium=referral&utm_content=codecentric/javafxsvg&utm_campaign=badger)

A simple library to add SVG support to JavaFX, allowing to use SVG
graphics just like any other image type.

More details about the project can be found at the codecentric blog: 
[Adding a custom image renderer to JavaFX 8](https://blog.codecentric.de/en/2015/03/adding-custom-image-renderer-javafx-8/)

## Installation

If you are using maven, just add JavaFxSVG to the dependencies

	<dependency>
		<groupId>de.codecentric.centerdevice</groupId>
		<artifactId>javafxsvg</artifactId>
		<version>1.3.0</version>
	</dependency>

## Usage

Add this line to your application:

    SvgImageLoaderFactory.install();
    
preferably before any JavaFX code is executed. After this, you can use 
SVG images just as any other Image in your application.

If the JavaFX container does not specify any width or height, the image is
rendered with default dimensions (currently 400x400). To changes this behavior,
a `DimensionProvider` can be passed with the install command: 

    SvgImageLoaderFactory.install(new PrimitiveDimensionProvider());

will try to determine the actual size of the SVG (as specified by `width` and 
`height` attributes) and use this as a fallback size.

## Known Issues

Currently, SVGs are required to start with either "<svg" or "<?xml" 
due to some rather static signature matching. As a result, svg might
not be displayed when starting with whitespace characters or comment.
