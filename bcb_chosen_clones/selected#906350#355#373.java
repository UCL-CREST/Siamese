    private void parseTtFont(String[] items) {
        int i = 2;
        if (!items[i].equals("path")) {
            _logger.warning("Path is not specified");
            return;
        } else {
            try {
                Font font = Font.createFont(Font.TRUETYPE_FONT, new File(items[i + 1]));
                GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
            } catch (FontFormatException e) {
                _logger.warning("Font is bad: " + items[i + 1]);
                return;
            } catch (IOException e) {
                _logger.warning("Can't load font: " + items[i + 1]);
                return;
            }
            i += 2;
        }
    }
