    private static void addImportFiles(Document document) {
        Element importElem;
        int lastChar = 0;
        Pattern pattern = Pattern.compile(";");
        Matcher matcher = pattern.matcher(OptionItems.IMPORT_FILE);
        while (matcher.find()) {
            importElem = document.createElement("xsl:import");
            importElem.setAttribute("href", OptionItems.IMPORT_FILE.substring(lastChar, matcher.start()).trim());
            lastChar = matcher.end();
            document.getDocumentElement().appendChild(importElem);
        }
        if (lastChar < OptionItems.IMPORT_FILE.trim().length()) {
            importElem = document.createElement("xsl:import");
            importElem.setAttribute("href", OptionItems.IMPORT_FILE.substring(lastChar, OptionItems.IMPORT_FILE.trim().length()));
            document.getDocumentElement().appendChild(importElem);
        }
    }
