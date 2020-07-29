    public static String getContent(String path, String encoding) throws IOException {
        URL url = new URL(path);
        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);
        InputStream inputStream = conn.getInputStream();
        InputStreamReader isr = new InputStreamReader(inputStream, encoding);
        StringBuffer sb = new StringBuffer();
        BufferedReader in = new BufferedReader(isr);
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            sb.append(inputLine);
            sb.append("\n");
        }
        return sb.toString();
    }
