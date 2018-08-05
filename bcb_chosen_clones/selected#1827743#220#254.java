    public byte[] getBytesMethod(String url) {
        logger.info("Facebook: @executing facebookGetMethod():" + url);
        byte[] responseBytes = null;
        try {
            HttpGet loginGet = new HttpGet(url);
            loginGet.addHeader("Accept-Encoding", "gzip");
            HttpResponse response = httpClient.execute(loginGet);
            HttpEntity entity = response.getEntity();
            logger.trace("Facebook: getBytesMethod: " + response.getStatusLine());
            if (entity != null) {
                InputStream in = response.getEntity().getContent();
                if (response.getEntity().getContentEncoding().getValue().equals("gzip")) {
                    in = new GZIPInputStream(in);
                }
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] b = new byte[4096];
                int n;
                while ((n = in.read(b)) != -1) {
                    out.write(b, 0, n);
                }
                responseBytes = out.toByteArray();
                in.close();
                entity.consumeContent();
            }
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                logger.warn("Facebook: Error Occured! Status Code = " + statusCode);
                responseBytes = null;
            }
            logger.info("Facebook: Get Bytes Method done(" + statusCode + "), response bytes length: " + (responseBytes == null ? 0 : responseBytes.length));
        } catch (IOException e) {
            logger.warn("Facebook: ", e);
        }
        return responseBytes;
    }
