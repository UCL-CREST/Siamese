    public static String addTableOfContents(String text, String contentTitle) {
        logger.debug("Start TOC generating...");
        Pattern p = Pattern.compile("<[Hh]([1-6])[^>]*>(.*?)</[Hh][1-6][^>]*>");
        Matcher m = p.matcher(text);
        StringBuffer result = new StringBuffer();
        MakeTableOfContents maketoc = new MakeTableOfContents();
        int position = 0;
        while (m.find()) {
            int level = Integer.parseInt(m.group(1));
            String header = m.group(2);
            String anchor = maketoc.makeAnchor(header);
            result.append(text.substring(position, m.start(2)));
            position = m.start(2);
            result.append("<a class=\"tocheader\" name=\"" + anchor + "\" id=\"" + anchor + "\"></a>");
            result.append(text.substring(position, m.end(2)));
            position = m.end(2);
            maketoc.addEntry(anchor, header, level);
            logger.debug("Adding content: " + header);
        }
        result.append(text.substring(position));
        if ((maketoc.size() >= Environment.getInstance().getTocMinimumHeaders()) && Environment.getInstance().isTocInsert()) {
            return maketoc.toHTML(contentTitle) + result.toString();
        }
        return result.toString();
    }
