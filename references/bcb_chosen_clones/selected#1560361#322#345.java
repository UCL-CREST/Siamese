    private <T extends APIObject> T executeApi(Class<T> returnclass, List<NameValuePair> getParams, List<NameValuePair> postParams) throws WikimediaApiException {
        ArrayList<NameValuePair> gets = new ArrayList<NameValuePair>(defaultparams);
        if (getParams != null) gets.addAll(getParams);
        gets.add(new BasicNameValuePair("requestid", "" + requestCounter++));
        String getParamString = URLEncodedUtils.format(gets, "utf-8");
        HttpUriRequest req = postParams == null ? new HttpGet(apiurl + "?" + getParamString) : new HttpPost(apiurl + "?" + getParamString);
        if (postParams != null) try {
            ((HttpPost) req).setEntity(new UrlEncodedFormEntity(postParams));
        } catch (UnsupportedEncodingException e) {
            throw new WikimediaApiException("IOError", e);
        }
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(execute(req).getEntity().getContent()));
            String line = in.readLine();
            T obj = returnclass.getConstructor(String.class).newInstance(line);
            if (obj.has("error")) {
                JSONObject err = (JSONObject) obj.get("error");
                throw new WikimediaApiException(err.get("code") + ": " + err.get("info"));
            }
            return obj;
        } catch (Exception exc) {
            throw new RuntimeException(exc);
        }
    }
