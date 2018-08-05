    FontResource(URI resourceLocator) throws Exception {
        super(resourceLocator);
        font = null;
        if (resourceLocator.getPath() != null && resourceLocator.getPath().endsWith(".ttf")) {
            URL url = resourceLocator.toURL();
            font = Font.createFont(Font.TRUETYPE_FONT, url.openStream());
        } else {
            String name = resourceLocator.getSchemeSpecificPart();
            font = Font.decode(name.substring(SCHEME.length()));
        }
        if (font != null) {
            try {
                GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
                Method registerFont = environment.getClass().getMethod("registerFont", Font.class);
                registerFont.invoke(environment, font);
            } catch (Exception e) {
                logger.warning("Failed to register font " + font.getName() + ": " + e.toString());
            }
        }
        logger.info("Loaded font: " + ((font == null) ? "(null)" : font.getFontName()) + " from: " + resourceLocator);
    }
