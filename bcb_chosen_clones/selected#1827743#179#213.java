    public String getMethod(String url) {
        logger.info("Facebook: @executing facebookGetMethod():" + url);
        String responseStr = null;
        try {
            HttpGet loginGet = new HttpGet(url);
            loginGet.addHeader("Accept-Encoding", "gzip");
            HttpResponse response = httpClient.execute(loginGet);
            HttpEntity entity = response.getEntity();
            logger.trace("Facebook: facebookGetMethod: " + response.getStatusLine());
            if (entity != null) {
                InputStream in = response.getEntity().getContent();
                if (response.getEntity().getContentEncoding().getValue().equals("gzip")) {
                    in = new GZIPInputStream(in);
                }
                StringBuffer sb = new StringBuffer();
                byte[] b = new byte[4096];
                int n;
                while ((n = in.read(b)) != -1) {
                    sb.append(new String(b, 0, n));
                }
                responseStr = sb.toString();
                in.close();
                entity.consumeContent();
            }
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                logger.warn("Facebook: Error Occured! Status Code = " + statusCode);
                responseStr = null;
            }
            logger.info("Facebook: Get Method done(" + statusCode + "), response string length: " + (responseStr == null ? 0 : responseStr.length()));
        } catch (IOException e) {
            logger.warn("Facebook: ", e);
        }
        return responseStr;
    }
