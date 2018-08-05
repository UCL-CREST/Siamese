    public static void loadCustomFont(String fontFile) {
        try {
            Font newFont = Font.createFont(Font.TRUETYPE_FONT, new File(Config.FONTS_DIR + fontFile));
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(newFont);
        } catch (Exception e) {
            System.out.println("ERROR: Cannot load " + fontFile + " font!");
            e.printStackTrace();
        }
    }
