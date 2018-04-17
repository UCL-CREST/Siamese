    private String filterReferences(String text) {
        String pattern = "\\`([^\\`]+)\\`";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(text);
        StringBuffer myStringBuffer = new StringBuffer();
        while (m.find()) {
            String ref = text.substring(m.start() + 1, m.end() - 1);
            m.appendReplacement(myStringBuffer, oxdoc.project.linkToSymbol(ref));
        }
        return m.appendTail(myStringBuffer).toString();
    }
