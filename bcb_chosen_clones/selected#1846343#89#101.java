    private static void loadAndRegisterFont(String fileName, String fontTypeName) throws MaxError {
        try {
            int fontType;
            Font newFont;
            if (fontTypeName.equals("trueType")) fontType = Font.TRUETYPE_FONT; else if (fontTypeName.equals("type1")) fontType = Font.TYPE1_FONT; else throw new FontManagerException("Font Error", "Unknown font type " + fontTypeName);
            newFont = Font.createFont(fontType, new File(fileName));
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(newFont);
        } catch (FontFormatException e) {
            throw new FontManagerException("Font Error", "Error in font file format", e);
        } catch (IOException e) {
            throw new FontManagerException("Font Error", "Cannot read font file", e);
        }
    }
