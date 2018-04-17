    public static String doPostWithBasicAuthentication(URL url, String username, String password, String parameters, Map<String, String> headers) throws IOException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setDoInput(true);
        con.setDoOutput(true);
        byte[] encodedPassword = (username + ":" + password).getBytes();
        BASE64Encoder encoder = new BASE64Encoder();
        con.setRequestProperty("Authorization", "Basic " + encoder.encode(encodedPassword));
        con.setConnectTimeout(2000);
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        if (parameters != null) con.setRequestProperty("Content-Length", "" + Integer.toString(parameters.getBytes().length));
        if (headers != null) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }
        }
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(parameters);
        wr.flush();
        wr.close();
        InputStream is = con.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuffer response = new StringBuffer();
        while ((line = rd.readLine()) != null) {
            response.append(line);
            response.append('\n');
        }
        rd.close();
        is.close();
        con.disconnect();
        return response.toString();
    }
