    String parseText(String text, String patternName, Hashtable<String, String> patterns) throws JDOMException, IOException {
        String docString = "<X>" + text.replace("&", "&amp;") + "</X>";
        Element e = org.exmaralda.common.jdomutilities.IOUtilities.readDocumentFromString(docString).getRootElement();
        String returnText = "";
        for (Object o : e.getContent()) {
            if (!(o instanceof Text)) {
                returnText += org.exmaralda.common.jdomutilities.IOUtilities.elementToString((Element) o);
                continue;
            }
            Pattern p = Pattern.compile(patterns.get(patternName));
            String thisText = ((Text) o).getText();
            Matcher m = p.matcher(thisText);
            int fromWhere = 0;
            while (m.find(fromWhere)) {
                int i1 = m.start();
                int i2 = m.end();
                thisText = thisText.substring(0, i1) + "<" + patternName + ">" + thisText.substring(i1, i2) + "</" + patternName + ">" + thisText.substring(i2);
                m = p.matcher(thisText);
                fromWhere = i2 + 2 * patternName.length() + 5;
            }
            returnText += thisText;
        }
        return returnText;
    }
