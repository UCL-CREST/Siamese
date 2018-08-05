    public static Vector getKeywordsFromURL(String p_url) throws Exception {
        URL x_url = new URL(p_url);
        URLConnection x_conn = x_url.openConnection();
        InputStreamReader x_is_reader = new InputStreamReader(x_conn.getInputStream());
        BufferedReader x_reader = new BufferedReader(x_is_reader);
        String x_line = null;
        String x_lc_line = null;
        Vector x_words = new Vector(1000);
        int x_body = -1;
        while ((x_line = x_reader.readLine()) != null) {
            x_lc_line = x_line.toLowerCase();
            x_body = x_lc_line.indexOf("<body");
            if (x_body != -1) {
                x_line = x_line.substring(x_body + 5);
                break;
            }
        }
        boolean x_status = false;
        int x_end = -1;
        if (x_lc_line == null) {
            System.out.println("No <body start");
            return null;
        }
        do {
            x_lc_line = x_line.toLowerCase();
            x_end = x_lc_line.indexOf("</body>");
            if (x_end != -1) {
                extractOutsideText(x_line.substring(0, x_end), "<", ">", x_words, x_status);
                break;
            }
            x_status = extractOutsideText(x_line, "<", ">", x_words, x_status);
        } while ((x_line = x_reader.readLine()) != null);
        return x_words;
    }
