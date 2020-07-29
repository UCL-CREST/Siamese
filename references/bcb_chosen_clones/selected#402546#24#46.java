    private static String doGetForSessionKey(String authCode) throws Exception {
        String sessionKey = "";
        HttpClient hc = new DefaultHttpClient();
        HttpGet hg = new HttpGet(Common.TEST_SESSION_HOST + Common.TEST_SESSION_PARAM + authCode);
        HttpResponse hr = hc.execute(hg);
        BufferedReader br = new BufferedReader(new InputStreamReader(hr.getEntity().getContent()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        String result = sb.toString();
        Log.i("sessionKeyMessages", result);
        Map<String, String> map = Util.handleURLParameters(result);
        sessionKey = map.get(Common.TOP_SESSION);
        String topParameters = map.get(Common.TOP_PARAMETERS);
        String decTopParameters = Util.decodeBase64(topParameters);
        Log.i("base64", decTopParameters);
        map = Util.handleURLParameters(decTopParameters);
        Log.i("nick", map.get(Common.VISITOR_NICK));
        CachePool.put(Common.VISITOR_NICK, map.get(Common.VISITOR_NICK));
        return sessionKey;
    }
