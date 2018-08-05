    public static String getHighlightBaseLib() throws Exception {
        StringBuffer highlightKey = new StringBuffer();
        highlightKey.append("<c color=\"" + COLOR_BASELIB + "\">\n\t");
        URL url = AbstractRunner.class.getResource("baselib.js");
        if (url != null) {
            InputStream is = url.openStream();
            InputStreamReader reader = new InputStreamReader(is, "UTF-8");
            BufferedReader bfReader = new BufferedReader(reader);
            String tmp = null;
            do {
                tmp = bfReader.readLine();
                if (tmp != null) {
                    if (tmp.indexOf("function") > -1) {
                        highlightKey.append("<w>" + (tmp.substring(tmp.indexOf("function") + 8, tmp.indexOf("(")).trim()) + "</w>\n\t");
                    }
                }
            } while (tmp != null);
            bfReader.close();
            reader.close();
            is.close();
        }
        highlightKey.append("</c>");
        return highlightKey.toString();
    }
