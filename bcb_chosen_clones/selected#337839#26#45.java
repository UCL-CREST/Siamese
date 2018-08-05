    @Override
    public void parse(Map<String, String> args, SHResourceManager manager) {
        File fontFile = new File(args.get(PATH_KEY));
        Font font = null;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
        } catch (IOException e) {
            _logger.warning("Can't load TrueType font from file " + fontFile);
            return;
        } catch (FontFormatException e) {
            _logger.warning("Wrong font format (FontFormatException): " + fontFile);
            return;
        }
        if (font != null) {
            manager.add(args.get(TYPE_KEY), font.getFontName(), font);
        } else {
            _logger.warning("Can't load font from file " + fontFile);
        }
    }
