    public void service(TranslationRequest request, TranslationResponse response) {
        try {
            Thread.sleep((long) Math.random() * 250);
        } catch (InterruptedException e1) {
        }
        hits.incrementAndGet();
        String key = getKey(request);
        RequestResponse cachedResponse = cache.get(key);
        if (cachedResponse == null) {
            response.setEndState(new ResponseStateBean(ResponseCode.ERROR, "response not found for " + key));
            return;
        }
        response.addHeaders(cachedResponse.getExpectedResponse().getHeaders());
        response.setTranslationCount(cachedResponse.getExpectedResponse().getTranslationCount());
        response.setFailCount(cachedResponse.getExpectedResponse().getFailCount());
        if (cachedResponse.getExpectedResponse().getLastModified() != -1) {
            response.setLastModified(cachedResponse.getExpectedResponse().getLastModified());
        }
        try {
            OutputStream output = response.getOutputStream();
            InputStream input = cachedResponse.getExpectedResponse().getInputStream();
            try {
                IOUtils.copy(input, output);
            } finally {
                IOUtils.closeQuietly(input);
                IOUtils.closeQuietly(output);
            }
        } catch (IOException e) {
            response.setEndState(new ResponseStateException(e));
            return;
        }
        response.setEndState(cachedResponse.getExpectedResponse().getEndState());
    }
