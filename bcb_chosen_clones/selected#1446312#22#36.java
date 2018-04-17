    private static void initFonts() {
        if (PLAIN_FONT == null || BOLD_FONT == null) {
            try {
                PLAIN_FONT = Font.createFont(Font.TRUETYPE_FONT, FontManagement.class.getResourceAsStream("font.ttf"));
                BOLD_FONT = Font.createFont(Font.TRUETYPE_FONT, FontManagement.class.getResourceAsStream("font_bold.ttf"));
                GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
                env.registerFont(PLAIN_FONT);
                env.registerFont(BOLD_FONT);
                FAMILY = PLAIN_FONT.getFamily();
                FAMILY_BOLD = BOLD_FONT.getFamily();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
