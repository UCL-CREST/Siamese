    public static void getResponseAsStream(String _url, Object _stringOrStream, OutputStream _stream, Map<String, String> _headers, Map<String, String> _params, String _contentType, int _timeout) throws IOException {
        if (_url == null || _url.length() <= 0) throw new IllegalArgumentException("Url can not be null.");
        String temp = _url.toLowerCase();
        if (!temp.startsWith("http://") && !temp.startsWith("https://")) _url = "http://" + _url;
        _url = encodeURL(_url);
        HttpMethod method = null;
        if (_stringOrStream == null && (_params == null || _params.size() <= 0)) method = new GetMethod(_url); else method = new PostMethod(_url);
        HttpMethodParams methodParams = ((HttpMethodBase) method).getParams();
        if (methodParams == null) {
            methodParams = new HttpMethodParams();
            ((HttpMethodBase) method).setParams(methodParams);
        }
        if (_timeout < 0) methodParams.setSoTimeout(0); else methodParams.setSoTimeout(_timeout);
        if (_contentType != null && _contentType.length() > 0) {
            if (_headers == null) _headers = new HashMap<String, String>();
            _headers.put("Content-Type", _contentType);
        }
        if (_headers == null || !_headers.containsKey("User-Agent")) {
            if (_headers == null) _headers = new HashMap<String, String>();
            _headers.put("User-Agent", DEFAULT_USERAGENT);
        }
        if (_headers != null) {
            Iterator<Map.Entry<String, String>> iter = _headers.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, String> entry = iter.next();
                method.setRequestHeader((String) entry.getKey(), (String) entry.getValue());
            }
        }
        if (method instanceof PostMethod && (_params != null && _params.size() > 0)) {
            Iterator<Map.Entry<String, String>> iter = _params.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, String> entry = iter.next();
                ((PostMethod) method).addParameter((String) entry.getKey(), (String) entry.getValue());
            }
        }
        if (method instanceof EntityEnclosingMethod && _stringOrStream != null) {
            if (_stringOrStream instanceof InputStream) {
                RequestEntity entity = new InputStreamRequestEntity((InputStream) _stringOrStream);
                ((EntityEnclosingMethod) method).setRequestEntity(entity);
            } else {
                RequestEntity entity = new StringRequestEntity(_stringOrStream.toString(), _contentType, null);
                ((EntityEnclosingMethod) method).setRequestEntity(entity);
            }
        }
        HttpClient httpClient = new HttpClient(new org.apache.commons.httpclient.SimpleHttpConnectionManager());
        httpClient.getParams().setBooleanParameter(HttpClientParams.ALLOW_CIRCULAR_REDIRECTS, true);
        InputStream instream = null;
        try {
            int status = httpClient.executeMethod(method);
            if (status != HttpStatus.SC_OK) {
                LOG.warn("Http Satus:" + status + ",Url:" + _url);
                if (status >= 500 && status < 600) throw new IOException("Remote service<" + _url + "> respose a error, status:" + status);
            }
            instream = method.getResponseBodyAsStream();
            IOUtils.copy(instream, _stream);
        } catch (IOException err) {
            LOG.error("Failed to access " + _url, err);
            throw err;
        } finally {
            IOUtils.closeQuietly(instream);
            if (method != null) method.releaseConnection();
        }
    }
