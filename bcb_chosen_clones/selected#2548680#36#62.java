    public static JSONObject doJSONQuery(String urlstr) throws IOException, MalformedURLException, JSONException, SolrException {
        URL url = new URL(urlstr);
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuffer buffer = new StringBuffer();
            String str;
            while ((str = in.readLine()) != null) {
                buffer.append(str + "\n");
            }
            in.close();
            JSONObject response = new JSONObject(buffer.toString());
            return response;
        } catch (IOException e) {
            if (con != null) {
                try {
                    int statusCode = con.getResponseCode();
                    if (statusCode >= 400) {
                        throw (new SolrSelectUtils()).new SolrException(statusCode);
                    }
                } catch (IOException exc) {
                }
            }
            throw (e);
        }
    }
