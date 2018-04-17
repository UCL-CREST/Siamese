    public static String sendGetRequest(String endpoint, String requestParameters) {
        if (endpoint == null) return null;
        String result = null;
        if (endpoint.startsWith("http://")) {
            try {
                StringBuffer data = new StringBuffer();
                String urlStr = endpoint;
                if (requestParameters != null && requestParameters.length() > 0) {
                    urlStr += "?" + requestParameters;
                }
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
            } catch (Exception e) {
                Logger.getLogger(HTTPClient.class.getClass().getName()).log(Level.FINE, "Could not connect to URL, is the service online?");
            }
        }
        return result;
    }
