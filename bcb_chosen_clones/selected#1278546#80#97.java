    public static String getURLContent(String urlToSearchString) throws IOException {
        URL url = new URL(urlToSearchString);
        URLConnection conn = url.openConnection();
        String encoding = conn.getContentEncoding();
        if (encoding == null) encoding = "ISO-8859-1";
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), encoding));
        StringBuilder sb = new StringBuilder(16384);
        try {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append('\n');
            }
        } finally {
            br.close();
        }
        return sb.toString();
    }
