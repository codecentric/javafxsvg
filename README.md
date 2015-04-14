# JavaFxSVG

A simple library to add SVG support to JavaFX, allowing to use SVG
graphics just like any other image type.

More details about the project can be found at the codecentric blog: 
[Adding a custom image renderer to JavaFX 8](https://blog.codecentric.de/en/2015/03/adding-custom-image-renderer-javafx-8/)

## Usage

Add this line to your application:

    SvgImageLoaderFactory.install();
    
preferably before any JavaFX code is executed. After this, you can use 
SVG images just as any other Image in your application.

## Known Issues

Currently, SVGs are required to start with either "<svg" or "<?xml" 
due to some rather static signature matching. As a result, svg might
not be displayed when starting with whitespace characters or comment.
