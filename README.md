# JavaFxSVG

A simple library to add SVG support to JavaFX, allowing to use SVG
graphics just like any other image type.

## Usage

Add this line to your application:

    SvgImageLoaderFactory.install();
    
preferably before any JavaFX code is executed. After this, you can use 
SVG images just as any other Image in your application.

## Known Issues

Currently, SVGs are required to start with either "<svg" or "<?xml" 
due to some rather static signature matching. As a result, svg might
not be displayed when starting with whitespace characters or comment.
