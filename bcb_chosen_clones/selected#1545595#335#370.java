    public static int addHiddenPostFields(String page, String startFromPattern, List<String> names, List<String> values) throws ConversationException {
        Pattern p;
        Matcher m;
        p = Pattern.compile(startFromPattern);
        m = p.matcher(page);
        if (!m.find()) {
            throw new ConversationException("Can't find start of form with pattern \"" + startFromPattern + "\"");
        }
        int startPos = m.start();
        p = Pattern.compile("</form>");
        m = p.matcher(page);
        m.region(startPos, page.length());
        int endPos = page.length();
        if (m.find()) {
            endPos = m.end();
        }
        p = Pattern.compile("<input(?=[^>]*type=\"hidden\")[^>]*name=\"(.*?)\"[^>]*value=\"(.*?)\"");
        m = p.matcher(page);
        m.region(startPos, endPos);
        while (m.find()) {
            String name = m.group(1);
            String value = m.group(2);
            names.add(name);
            values.add(value);
        }
        p = Pattern.compile("<input(?=[^>]*type=\"hidden\")[^>]*value=\"(.*?)\"[^>]*name=\"(.*?)\"");
        m = p.matcher(page);
        m.region(startPos, endPos);
        while (m.find()) {
            String name = m.group(1);
            String value = m.group(2);
            names.add(name);
            values.add(value);
        }
        return startPos;
    }
