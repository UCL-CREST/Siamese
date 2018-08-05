    private JSONObject executeHttpGet(String uri) throws Exception {
        HttpGet req = new HttpGet(uri);
        HttpClient client = new DefaultHttpClient();
        HttpResponse resLogin = client.execute(req);
        BufferedReader r = new BufferedReader(new InputStreamReader(resLogin.getEntity().getContent()));
        StringBuilder sb = new StringBuilder();
        String s = null;
        while ((s = r.readLine()) != null) {
            sb.append(s);
        }
        return new JSONObject(sb.toString());
    }
