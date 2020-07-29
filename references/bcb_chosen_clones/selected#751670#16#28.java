    public static String getURLContent(String urlStr) throws MalformedURLException, IOException {
        URL url = new URL(urlStr);
        log.info("url: " + url);
        URLConnection conn = url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuffer buf = new StringBuffer();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            buf.append(inputLine);
        }
        in.close();
        return buf.toString();
    }
