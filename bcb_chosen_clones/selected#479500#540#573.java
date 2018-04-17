    public Font getFont() {
        if (loadedFont != null) {
            return loadedFont;
        }
        Font font = new Font("SansSerif", Font.PLAIN, 1);
        int fontsize = 11;
        try {
            Font base = font;
            String fontname = readString("gui.font.name");
            String fontfilename = readString("gui.font.file");
            String fontsizestr = readString("gui.font.size");
            if (fontfilename.length() != 0) {
                File fontfile = new File(fontfilename);
                if (fontfile.exists()) {
                    base = Font.createFont(Font.TRUETYPE_FONT, fontfile);
                } else {
                    System.err.println("gui.font.file '" + fontfilename + "' doesn't exist.");
                }
            } else if (fontname.length() != 0) {
                base = new Font(fontname, Font.PLAIN, 1);
            }
            if (fontsizestr.length() > 0) fontsize = Integer.parseInt(fontsizestr); else System.err.println("gui.font.size setting is missing.");
            font = base.deriveFont(Font.PLAIN, fontsize);
            java.awt.GraphicsEnvironment ge = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(base);
        } catch (NumberFormatException e) {
            System.err.println("gui.font.size setting is invalid.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        loadedFont = font;
        return font;
    }
