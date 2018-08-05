    public void loadFont(String path) {
        InputStream is = null;
        try {
            is = packed.getFile(path);
            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            if (validating) return;
            if (!GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font)) LOGGER.log(Level.FINE, "Unable to register the font in file: " + path);
        } catch (FontFormatException e) {
            LOGGER.log(Level.WARNING, "Invalid font: " + path, e);
            throw new IllegalArgumentException("Invalid font file: " + path, e);
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.WARNING, "Unknown file: " + path, e);
            throw new IllegalArgumentException("Invalid resource: " + path);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Invalid font file: " + path, e);
            throw new IllegalArgumentException("Invalid font file: " + path, e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOGGER.log(Level.WARNING, "Ignoring font close exception", e);
                }
            }
        }
    }
