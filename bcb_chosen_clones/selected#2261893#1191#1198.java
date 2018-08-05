    private void registerStandardFont(String url) {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream(url));
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
        } catch (Throwable xp) {
            log(Level.WARNING, xp, Text.get("Can''t register font ''{0}''.", url));
        }
    }
