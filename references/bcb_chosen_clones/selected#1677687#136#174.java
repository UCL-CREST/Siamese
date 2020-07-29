    public Object invoke(MethodInvocation invocation, int retryTimes) throws Throwable {
        retryTimes--;
        try {
            String url = getServiceUrl() + "/" + invocation.getMethod().getName();
            HttpPost postMethod = new HttpPost(url);
            if (invocation.getMethod().getParameterTypes().length > 0) postMethod.setEntity(new StringEntity(JsonUtils.toJson(invocation.getArguments())));
            HttpResponse rsp = HttpClientUtils.getDefaultInstance().execute(postMethod);
            StatusLine sl = rsp.getStatusLine();
            if (sl.getStatusCode() >= 300) {
                throw new RuntimeException("Did not receive successful HTTP response: status code = " + sl.getStatusCode() + ", status message = [" + sl.getReasonPhrase() + "]");
            }
            HttpEntity entity = rsp.getEntity();
            StringBuilder sb = new StringBuilder();
            InputStream is = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
            String line;
            while ((line = reader.readLine()) != null) sb.append(line).append("\n");
            reader.close();
            is.close();
            String responseBody = null;
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
                responseBody = sb.toString();
            }
            Type t = invocation.getMethod().getGenericReturnType();
            if (t.equals(Void.class) || responseBody == null) return null;
            return JsonUtils.fromJson(responseBody, t);
        } catch (ConnectTimeoutException e) {
            if (retryTimes < 0) throw e;
            if (urlFromDiscovery) {
                String serviceUrl = discoverServiceUrl(getServiceInterface().getName());
                if (!serviceUrl.equals(getServiceUrl())) {
                    setServiceUrl(serviceUrl);
                    log.info("relocate service url:" + serviceUrl);
                }
            }
            return invoke(invocation, retryTimes);
        }
    }
