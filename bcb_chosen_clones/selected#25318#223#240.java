    private static String sendGetRequest(String endpoint, String requestParameters) throws Exception {
        String result = null;
        if (endpoint.startsWith("http://")) {
            StringBuffer data = new StringBuffer();
            String urlStr = prepareUrl(endpoint, requestParameters);
            URL url = new URL(urlStr);
            URLConnection conn = url.openConnection();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            result = sb.toString();
        }
        return result;
    }
