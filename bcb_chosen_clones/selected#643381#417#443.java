    public static Vector getMetaKeywordsFromURL(String p_url) throws Exception {
        URL x_url = new URL(p_url);
        URLConnection x_conn = x_url.openConnection();
        InputStreamReader x_is_reader = new InputStreamReader(x_conn.getInputStream());
        BufferedReader x_reader = new BufferedReader(x_is_reader);
        String x_line = null;
        String x_lc_line = null;
        int x_body = -1;
        String x_keyword_list = null;
        int x_keywords = -1;
        String[] x_meta_keywords = null;
        while ((x_line = x_reader.readLine()) != null) {
            x_lc_line = x_line.toLowerCase();
            x_keywords = x_lc_line.indexOf("<meta name=\"keywords\" content=\"");
            if (x_keywords != -1) {
                x_keywords = "<meta name=\"keywords\" content=\"".length();
                x_keyword_list = x_line.substring(x_keywords, x_line.indexOf("\">", x_keywords));
                x_keyword_list = x_keyword_list.replace(',', ' ');
                x_meta_keywords = Parser.extractWordsFromSpacedList(x_keyword_list);
            }
            x_body = x_lc_line.indexOf("<body");
            if (x_body != -1) break;
        }
        Vector x_vector = new Vector(x_meta_keywords.length);
        for (int i = 0; i < x_meta_keywords.length; i++) x_vector.add(x_meta_keywords[i]);
        return x_vector;
    }
