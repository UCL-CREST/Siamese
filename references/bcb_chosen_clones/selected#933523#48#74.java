    static Object executeMethod(HttpMethod method, int timeout, boolean array) throws HttpRequestFailureException, HttpException, IOException, HttpRequestTimeoutException {
        try {
            method.getParams().setSoTimeout(timeout * 1000);
            int status = -1;
            Object result = null;
            System.out.println("Execute method: " + method.getPath() + " " + method.getQueryString());
            TwitterclipseConfig config = TwitterclipsePlugin.getDefault().getTwitterclipseConfiguration();
            HttpClient httpClient = HttpClientUtils.createHttpClient(TWITTER_BASE_URL, config.getUserId(), config.getPassword());
            status = httpClient.executeMethod(method);
            System.out.println("Received response. status = " + status);
            if (status == HttpStatus.SC_OK) {
                InputStream inputStream = method.getResponseBodyAsStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                IOUtils.copy(inputStream, baos);
                String response = new String(baos.toByteArray(), "UTF-8");
                System.out.println(response);
                if (array) result = JSONArray.fromString(response); else result = JSONObject.fromString(response);
            } else {
                throw new HttpRequestFailureException(status);
            }
            return result;
        } catch (SocketTimeoutException e) {
            throw new HttpRequestTimeoutException(e);
        } finally {
            method.releaseConnection();
        }
    }
