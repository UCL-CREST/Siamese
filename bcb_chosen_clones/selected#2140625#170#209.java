    public static void loadFonts() {
        fontNames = new ArrayList<String>();
        fontsMap = new HashMap<String, HashMap<Float, Font>>();
        ClassLoader classLoader = FontFactory.class.getClassLoader();
        InputStream xmlIs = classLoader.getResourceAsStream(XMLFILE);
        Document fontDoc = XMLParserUtility.parseXmlFile(xmlIs);
        Element root = fontDoc.getDocumentElement();
        NodeList nodes = root.getElementsByTagName("font");
        if (nodes != null && nodes.getLength() > 0) {
            for (int i = 0; i < nodes.getLength(); i++) {
                Element node = (Element) nodes.item(i);
                String fontFile = XMLParserUtility.getTextFromElement(node, "file");
                String fontName = null;
                try {
                    StringBuffer fullPath = new StringBuffer();
                    fullPath.append(DIR_FONTS);
                    fullPath.append(fontFile);
                    InputStream is = classLoader.getResourceAsStream(fullPath.toString());
                    fontName = getPrefix(fontFile).toLowerCase();
                    System.out.println("loading font " + fontName);
                    Font newFont = Font.createFont(Font.TRUETYPE_FONT, is);
                    is.close();
                    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                    ge.registerFont(newFont);
                    HashMap<Float, Font> fontMap = new HashMap<Float, Font>();
                    for (int j = 0; j < FONT_SIZES.length; j++) {
                        float size = FONT_SIZES[j];
                        fontMap.put(new Float(size), newFont.deriveFont(size));
                    }
                    fontsMap.put(fontName, fontMap);
                    fontNames.add(fontName);
                    System.out.println("complete!");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println(ex.getMessage());
                    System.exit(1);
                }
            }
        }
    }
