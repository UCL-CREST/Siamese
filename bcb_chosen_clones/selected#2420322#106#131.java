    public String get(String s, String encoding) throws Exception {
        if (!s.startsWith("http")) return "";
        StringBuilder sb = new StringBuilder();
        try {
            String result = null;
            URL url = new URL(s);
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)");
            connection.setDoOutput(false);
            if (encoding == null) encoding = "UTF-8";
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), encoding));
            String inputLine;
            String contentType = connection.getContentType();
            if (contentType.startsWith("text") || contentType.startsWith("application/xml")) {
                while ((inputLine = in.readLine()) != null) {
                    sb.append(inputLine);
                    sb.append("\n");
                }
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return sb.toString();
    }
