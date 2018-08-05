    public static String getDocumentAsString(URL url) throws IOException {
        StringBuffer result = new StringBuffer();
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF8"));
        String line = "";
        while (line != null) {
            result.append(line);
            line = in.readLine();
        }
        return result.toString();
    }
