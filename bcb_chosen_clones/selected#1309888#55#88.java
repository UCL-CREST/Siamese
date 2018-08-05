    public static String addTableOfContents(String text) {
        logger.debug("Start TOC generating...");
        Pattern p = Pattern.compile("<[Hh][123][^>]*>(.*)</[Hh][123][^>]*>");
        Matcher m = p.matcher(text);
        StringBuffer result = new StringBuffer();
        StringBuffer toc = new StringBuffer();
        toc.append("<table align=\"right\" class=\"toc\"><tr><td>");
        int position = 0;
        while (m.find()) {
            result.append(text.substring(position, m.start(1)));
            position = m.start(1);
            result.append("<a class=\"tocheader\" name=\"" + position + "\" id=\"" + position + "\"></a>");
            if (m.group().startsWith("<h1") || m.group().startsWith("<H1")) {
                toc.append("<span class=\"tocheader1\">");
            } else if (m.group().startsWith("<h2") || m.group().startsWith("<H2")) {
                toc.append("<span class=\"tocheader2\">");
            } else {
                toc.append("<span class=\"tocheader3\">");
            }
            toc.append("<li><a href=\"#" + position + "\">" + m.group(1) + "</a></li></span>");
            result.append(text.substring(position, m.end(1)));
            position = m.end(1);
            logger.debug("Adding content: " + m.group(1));
        }
        toc.append("</td></tr></table>");
        result.append(text.substring(position));
        if (position > 0) {
            logger.debug("adding TOC at the beginning!");
            toc.append(result);
        } else {
            toc = result;
        }
        return toc.toString();
    }
