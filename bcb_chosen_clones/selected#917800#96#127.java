    @SuppressWarnings("unchecked")
    public TBXElement createTBXElement(String tagName, Locator loc) throws SAXParseException {
        if (errorChecking && !isXMLName(tagName, true)) super.createElement(tagName);
        TBXElement ret = null;
        try {
            try {
                Class<TBXElement> clazz = (Class<TBXElement>) Class.forName(PREFIX + tagName, true, getClass().getClassLoader());
                Constructor<TBXElement> cstrct = clazz.getConstructor(TBXDocument.class, String.class, Locator.class);
                ret = cstrct.newInstance(this, tagName, loc);
            } catch (ClassNotFoundException err) {
                if (!TBXParser.KNOWN_MISSING.contains(tagName)) Logger.getLogger("org.ttt.salt.dom.tbx").log(Level.INFO, "Unknown TBX Element: {0}", tagName);
                ret = new TBXElement(this, tagName, loc);
            }
            if (tagName.equals("martif")) {
                docElement = ret;
                if (!hardRef) tbxDocElem = new TBXElementDocument(this, tagName, loc, docElement);
            }
        } catch (NoSuchMethodException err) {
            Logger.getLogger("org.ttt.salt.dom.tbx").log(Level.SEVERE, "Invalid TBX Class: {0}", tagName);
            throw new SAXParseException("Invalid TBX Class", loc, err);
        } catch (InstantiationException err) {
            Logger.getLogger("org.ttt.salt.dom.tbx").log(Level.SEVERE, "TBX class not concrete: {0}", tagName);
            throw new SAXParseException("TBX class not concrete", loc, err);
        } catch (IllegalAccessException err) {
            Logger.getLogger("org.ttt.salt.dom.tbx").log(Level.SEVERE, "TBX constructor not accessible on {0}", tagName);
            throw new SAXParseException("TBX constructor not accessible", loc, err);
        } catch (InvocationTargetException err) {
            Logger.getLogger("org.ttt.salt.dom.tbx").log(Level.SEVERE, "TBX element {0} creation error", tagName);
            throw new SAXParseException("TBX element creation error", loc, err);
        }
        return ret;
    }
