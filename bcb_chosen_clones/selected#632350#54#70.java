    protected HttpResponseImpl makeRequest(final HttpMethod m, final String requestId) {
        try {
            HttpResponseImpl ri = new HttpResponseImpl();
            ri.setRequestMethod(m);
            ri.setResponseCode(_client.executeMethod(m));
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            IOUtils.copy(m.getResponseBodyAsStream(), bos);
            ri.setResponseBody(bos.toByteArray());
            notifyOfRequestSuccess(requestId, m, ri);
            return ri;
        } catch (HttpException ex) {
            notifyOfRequestFailure(requestId, m, ex);
        } catch (IOException ex) {
            notifyOfRequestFailure(requestId, m, ex);
        }
        return null;
    }
