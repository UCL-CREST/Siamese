    public static String getHtml(DefaultHttpClient httpclient, String url, String encode) throws IOException {
        InputStream input = null;
        HttpGet get = new HttpGet(url);
        HttpResponse res = httpclient.execute(get);
        StatusLine status = res.getStatusLine();
        if (status.getStatusCode() != STATUSCODE_200) {
            throw new RuntimeException("50001");
        }
        if (res.getEntity() == null) {
            return "";
        }
        input = res.getEntity().getContent();
        InputStreamReader reader = new InputStreamReader(input, encode);
        BufferedReader bufReader = new BufferedReader(reader);
        String tmp = null, html = "";
        while ((tmp = bufReader.readLine()) != null) {
            html += tmp;
        }
        if (input != null) {
            input.close();
        }
        return html;
    }
