    private void registerGameFonts() {
        final Set<String> fontFilenames = listFilesOfDirectory(FONT_PATH, new FontFilenameFilter());
        final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        for (final String s : fontFilenames) {
            LOGGER.info(buildLogString(FONTS_FOUND_FROM_RESOURCES_INFO, s));
            InputStream ttf = null;
            try {
                ttf = new FileInputStream(FONT_PATH + s);
                try {
                    final Font font = createFont(TRUETYPE_FONT, ttf);
                    ge.registerFont(font);
                } catch (final FontFormatException e) {
                    LOGGER.log(SEVERE, buildLogString(FONTS_FILE_FORMAT_ERROR, FONT_PATH + s), e);
                }
            } catch (final IOException e) {
                LOGGER.log(SEVERE, buildLogString(FONTS_FILE_NOT_FOUND_FROM_RESOURCES_ERROR, FONT_PATH + s), e);
            } finally {
                if (ttf != null) {
                    try {
                        ttf.close();
                    } catch (final IOException e) {
                        LOGGER.log(SEVERE, buildLogString(CLOSE_FILE_ERROR, FONT_PATH + fontFilenames), e);
                    }
                }
            }
        }
    }
