    private static StringBuilder deString(String aToken, StringBuilder aPage, String aLangCode, boolean jsEscape) {
        Pattern p = Pattern.compile(aToken);
        Matcher m = p.matcher(aPage);
        String aCultString = "";
        try {
            BlitzCulture aCulture = (BlitzCulture) BlitzCulture.loadedCultures.get(aLangCode);
            while (m.find()) {
                if (jsEscape) {
                    aCultString = aCulture.getString(m.group(2));
                    aCultString = _jsEscapeValue(aCultString);
                } else {
                    aCultString = aCulture.getString(m.group(2));
                }
                if (BlitzServer.labelStrings == 1) aPage.replace(m.start(1), m.end(1), aCultString + "[" + m.group(2) + "]"); else aPage.replace(m.start(1), m.end(1), aCultString);
            }
        } catch (Exception e) {
            ;
        }
        return aPage;
    }
