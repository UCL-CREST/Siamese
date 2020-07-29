    private synchronized void installFontFile(String fileName, CoFontFaceSpec spec) throws CoFontException {
        m_shower.showStatus("Installing font " + spec + " from file " + fileName);
        File file;
        try {
            file = new ClassPathResource(fileName).getFile();
        } catch (IOException e) {
            throw new CoFontException(e);
        }
        CoFontFileInfoExtractor parser = CoAbstractFontFileInfoExtractor.parseFontFile(file);
        CoFontMetricsData metrics = parser.getMetricsData();
        CoFontPostscriptData postscriptData = parser.getPostscriptData();
        CoFontFileContainer fileContainer = parser.getFileContainer();
        CoFontAwtData awtData = parser.suggestedAwtData();
        if (parser instanceof CoType1FileInfoExtractor) {
            awtData.awtWorkaround_setType1(true);
        }
        if (fileName.toLowerCase().endsWith(".ttf")) {
            Font font;
            try {
                font = Font.createFont(Font.TRUETYPE_FONT, file);
            } catch (FontFormatException e) {
                throw new CoFontException(e);
            } catch (IOException e) {
                throw new CoFontException(e);
            }
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
        }
        installFontFace(spec, metrics, postscriptData, awtData, fileContainer);
    }
