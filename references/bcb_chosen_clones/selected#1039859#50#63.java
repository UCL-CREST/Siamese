    public void loadShippedFonts() throws Exception {
        String fontPath = "data/fonts";
        Set<String> ttfFiles = IO.listDataFiles(fontPath, FileUtils.getTTFFilter());
        for (String file : ttfFiles) {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File(fontPath + "/" + file));
            String fontName = font.getFamily();
            if (!isFontFamilySupported(fontName)) {
                Log.message("Registering font: " + fontName);
                supportedFontFamiliesSorted.add(fontName);
                supportedFontFamilies.add(fontName);
                GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
            }
        }
    }
