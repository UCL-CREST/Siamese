    public static String checkXMLTagsLowerCase(String originalXML) {
        String regExpOfTagName = "<[^<>' ']*";
        String regExpOfAttributeName = "[^' ']*=\"|[^' ']*=\'";
        Pattern p1 = Pattern.compile(regExpOfTagName);
        Pattern p2 = Pattern.compile(regExpOfAttributeName);
        Matcher m1 = p1.matcher(originalXML);
        Matcher m2 = p2.matcher(originalXML);
        while (m1.find()) {
            String partToReplace = originalXML.substring(m1.start(), m1.end());
            String firstPart = "", lastPart = "";
            if (m1.start() > 0) firstPart = originalXML.substring(0, m1.start());
            if (m1.end() < originalXML.length()) lastPart = originalXML.substring(m1.end(), originalXML.length());
            originalXML = firstPart + partToReplace.toLowerCase() + lastPart;
        }
        while (m2.find()) {
            String partToReplace = originalXML.substring(m2.start(), m2.end());
            String firstPart = "", lastPart = "";
            if (m2.start() > 0) firstPart = originalXML.substring(0, m2.start());
            if (m2.end() < originalXML.length()) lastPart = originalXML.substring(m2.end(), originalXML.length());
            originalXML = firstPart + partToReplace.toLowerCase() + lastPart;
        }
        return originalXML;
    }
