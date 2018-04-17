    public VisualConfiguration(org.w3c.dom.Node n, String fontDir) throws XMLtoWorldException {
        this();
        if (!(n instanceof org.w3c.dom.Element)) {
            throw (new XMLtoWorldException("VisualConfiguration node not Element"));
        }
        org.w3c.dom.Element e = (org.w3c.dom.Element) n;
        if (!e.getTagName().equalsIgnoreCase("VisualConfiguration")) {
            throw (new XMLtoWorldException("Element not named VisualConfiguration as expected"));
        }
        org.w3c.dom.NodeList nl = e.getElementsByTagName("Colors");
        if (nl.getLength() > 0) {
            org.w3c.dom.Element el = (org.w3c.dom.Element) nl.item(0);
            org.w3c.dom.Element elt1;
            org.w3c.dom.NodeList nl1 = el.getElementsByTagName("Description");
            if (nl1.getLength() > 0) {
                elt1 = (org.w3c.dom.Element) nl1.item(0);
                if (elt1.hasAttribute("color")) {
                    if (elt1.getAttribute("color").charAt(0) == '%') colorCodesTable.put("description", elt1.getAttribute("color")); else colorCodesTable.put("description", "%" + elt1.getAttribute("color") + "%");
                }
            }
            nl1 = el.getElementsByTagName("Important");
            if (nl1.getLength() > 0) {
                elt1 = (org.w3c.dom.Element) nl1.item(0);
                if (elt1.hasAttribute("color")) {
                    if (elt1.getAttribute("color").charAt(0) == '%') colorCodesTable.put("important", elt1.getAttribute("color")); else colorCodesTable.put("important", "%" + elt1.getAttribute("color") + "%");
                }
            }
            nl1 = el.getElementsByTagName("Action");
            if (nl1.getLength() > 0) {
                elt1 = (org.w3c.dom.Element) nl1.item(0);
                if (elt1.hasAttribute("color")) {
                    if (elt1.getAttribute("color").charAt(0) == '%') colorCodesTable.put("action", elt1.getAttribute("color")); else colorCodesTable.put("action", "%" + elt1.getAttribute("color") + "%");
                }
            }
            nl1 = el.getElementsByTagName("Information");
            if (nl1.getLength() > 0) {
                elt1 = (org.w3c.dom.Element) nl1.item(0);
                if (elt1.hasAttribute("color")) {
                    if (elt1.getAttribute("color").charAt(0) == '%') colorCodesTable.put("information", elt1.getAttribute("color")); else colorCodesTable.put("information", "%" + elt1.getAttribute("color") + "%");
                }
            }
            nl1 = el.getElementsByTagName("Denial");
            if (nl1.getLength() > 0) {
                elt1 = (org.w3c.dom.Element) nl1.item(0);
                if (elt1.hasAttribute("color")) {
                    if (elt1.getAttribute("color").charAt(0) == '%') colorCodesTable.put("denial", elt1.getAttribute("color")); else colorCodesTable.put("denial", "%" + elt1.getAttribute("color") + "%");
                }
            }
            nl1 = el.getElementsByTagName("Error");
            if (nl1.getLength() > 0) {
                elt1 = (org.w3c.dom.Element) nl1.item(0);
                if (elt1.hasAttribute("color")) {
                    if (elt1.getAttribute("color").charAt(0) == '%') colorCodesTable.put("error", elt1.getAttribute("color")); else colorCodesTable.put("error", "%" + elt1.getAttribute("color") + "%");
                }
            }
            nl1 = el.getElementsByTagName("Story");
            if (nl1.getLength() > 0) {
                elt1 = (org.w3c.dom.Element) nl1.item(0);
                if (elt1.hasAttribute("color")) {
                    if (elt1.getAttribute("color").charAt(0) == '%') colorCodesTable.put("story", elt1.getAttribute("color")); else colorCodesTable.put("story", "%" + elt1.getAttribute("color") + "%");
                }
            }
            nl1 = el.getElementsByTagName("Default");
            if (nl1.getLength() > 0) {
                elt1 = (org.w3c.dom.Element) nl1.item(0);
                if (elt1.hasAttribute("color")) {
                    if (elt1.getAttribute("color").charAt(0) == '%') colorCodesTable.put("default", elt1.getAttribute("color")); else colorCodesTable.put("default", "%" + elt1.getAttribute("color") + "%");
                }
            }
            nl1 = el.getElementsByTagName("Input");
            if (nl1.getLength() > 0) {
                elt1 = (org.w3c.dom.Element) nl1.item(0);
                if (elt1.hasAttribute("color")) {
                    Debug.println("Element:" + elt1);
                    if (elt1.hasAttribute("color")) {
                        if (elt1.getAttribute("color").charAt(0) == '%') colorCodesTable.put("input", elt1.getAttribute("color")); else colorCodesTable.put("input", "%" + elt1.getAttribute("color") + "%");
                    }
                    Debug.println("Visconf init with input color" + colorCodesTable.get("input"));
                }
            }
            nl1 = el.getElementsByTagName("Foreground");
            if (nl1.getLength() > 0) {
                elt1 = (org.w3c.dom.Element) nl1.item(0);
                if (elt1.hasAttribute("color")) {
                    Color c = stringToColor(elt1.getAttribute("color"));
                    if (c != null) foreground = c;
                }
            }
            nl1 = el.getElementsByTagName("Background");
            if (nl1.getLength() > 0) {
                elt1 = (org.w3c.dom.Element) nl1.item(0);
                if (elt1.hasAttribute("color")) {
                    Color c = stringToColor(elt1.getAttribute("color"));
                    if (c != null) background = c;
                }
            }
        }
        nl = e.getElementsByTagName("Font");
        boolean usingDefaultFont = true;
        if (nl.getLength() > 0) {
            fontName = "Courier New";
            fontSize = (float) 12.0;
            org.w3c.dom.Element el = (org.w3c.dom.Element) nl.item(0);
            if (el.hasAttribute("name")) fontName = el.getAttribute("name");
            if (el.hasAttribute("size")) fontSize = Float.valueOf(el.getAttribute("size")).floatValue();
            Font[] fuentes = null;
            try {
                fuentes = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
            } catch (AWTError err) {
                System.err.println("Warning: couldn't get fonts from local graphics environment");
                return;
            }
            Font fuenteElegida;
            for (int f = 0; f < fuentes.length; f++) {
                if (fuentes[f].getFontName().equalsIgnoreCase(fontName)) {
                    laFuente = fuentes[f].deriveFont((float) fontSize);
                    usingDefaultFont = false;
                    break;
                }
            }
            if (el.hasAttribute("filename")) {
                try {
                    fontFileName = el.getAttribute("filename");
                    Debug.println("Font filename: " + fontFileName);
                    Debug.println("Font directory: " + fontDir);
                    String f;
                    if (fontDir != null) {
                        f = fontDir + fontFileName;
                    } else {
                        f = fontFileName;
                    }
                    java.io.InputStream is = URLUtils.openFileOrURL(f);
                    Font fuente = Font.createFont(Font.TRUETYPE_FONT, is);
                    laFuente = fuente.deriveFont((float) fontSize);
                    GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(laFuente);
                    usingDefaultFont = false;
                } catch (Exception ex) {
                    Debug.println(ex);
                }
            }
            if (usingDefaultFont && el.hasAttribute("size") && laFuente != null) laFuente = laFuente.deriveFont((float) fontSize);
        }
    }
