    private static void installFont(String fontName, File ttfFile) {
        try {
            if (getFontsMap(false).containsKey(fontName)) {
                LOG.info(fontName + " font is already registered.");
                return;
            }
            Font font = Font.createFont(Font.TRUETYPE_FONT, ttfFile);
            if (font == null) {
                LOG.warning(fontName + " font could not be created.");
                return;
            }
            if (!GRAPHICS_ENVIRONMENT.registerFont(font)) {
                LOG.warning(fontName + " font was not registered.");
                return;
            }
            LOG.info(fontName + " font was registered.");
        } catch (Exception ex) {
            LOG.warning(fontName + " font could not be registered.");
        }
    }
