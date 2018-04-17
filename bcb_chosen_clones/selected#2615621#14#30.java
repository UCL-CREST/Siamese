    public static Font registerFont(String FontName) {
        InputStream inputStream = resource.class.getResourceAsStream(FontName);
        Font f;
        try {
            f = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
            graphicsEnvironment.registerFont(f);
            return f;
        } catch (Exception ex) {
            return null;
        } finally {
            try {
                inputStream.close();
            } catch (IOException ex) {
            }
        }
    }
