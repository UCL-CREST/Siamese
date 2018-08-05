    public void serviceDocument(final TranslationRequest request, final TranslationResponse response, final Document document) throws Exception {
        response.addHeaders(document.getResponseHeaders());
        try {
            IOUtils.copy(document.getInputStream(), response.getOutputStream());
            response.setEndState(ResponseStateOk.getInstance());
        } catch (Exception e) {
            response.setEndState(new ResponseStateException(e));
            log.warn("Error parsing XML of " + document, e);
        }
    }
