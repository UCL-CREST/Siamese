    protected String replaceQuotedStrings(String inputValue, Vector<String> retVars) {
        retVars.clear();
        Pattern p = Pattern.compile("'[[^']*]*'");
        Matcher m = p.matcher(inputValue);
        int i = 0;
        StringBuffer retValue = new StringBuffer(inputValue.length());
        while (m.find()) {
            retVars.addElement(new String(inputValue.substring(m.start(), m.end())));
            m.appendReplacement(retValue, "<--" + i + "-->");
            i++;
        }
        m.appendTail(retValue);
        return retValue.toString();
    }
