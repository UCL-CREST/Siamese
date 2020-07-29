    @Override
    protected void doFetch(HttpServletRequest request, HttpServletResponse response) throws IOException, GadgetException {
        if (request.getHeader("If-Modified-Since") != null) {
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            return;
        }
        String host = request.getHeader("Host");
        if (!lockedDomainService.isSafeForOpenProxy(host)) {
            String msg = "Embed request for url " + getParameter(request, URL_PARAM, "") + " made to wrong domain " + host;
            logger.info(msg);
            throw new GadgetException(GadgetException.Code.INVALID_PARAMETER, msg);
        }
        HttpRequest rcr = buildHttpRequest(request, URL_PARAM);
        HttpResponse results = requestPipeline.execute(rcr);
        if (results.isError()) {
            HttpRequest fallbackRcr = buildHttpRequest(request, FALLBACK_URL_PARAM);
            if (fallbackRcr != null) {
                results = requestPipeline.execute(fallbackRcr);
            }
        }
        if (contentRewriterRegistry != null) {
            try {
                results = contentRewriterRegistry.rewriteHttpResponse(rcr, results);
            } catch (RewritingException e) {
                throw new GadgetException(GadgetException.Code.INTERNAL_SERVER_ERROR, e);
            }
        }
        for (Map.Entry<String, String> entry : results.getHeaders().entries()) {
            String name = entry.getKey();
            if (!DISALLOWED_RESPONSE_HEADERS.contains(name.toLowerCase())) {
                response.addHeader(name, entry.getValue());
            }
        }
        String responseType = results.getHeader("Content-Type");
        if (!StringUtils.isEmpty(rcr.getRewriteMimeType())) {
            String requiredType = rcr.getRewriteMimeType();
            if (requiredType.endsWith("/*") && !StringUtils.isEmpty(responseType)) {
                requiredType = requiredType.substring(0, requiredType.length() - 2);
                if (!responseType.toLowerCase().startsWith(requiredType.toLowerCase())) {
                    response.setContentType(requiredType);
                    responseType = requiredType;
                }
            } else {
                response.setContentType(requiredType);
                responseType = requiredType;
            }
        }
        setResponseHeaders(request, response, results);
        if (results.getHttpStatusCode() != HttpResponse.SC_OK) {
            response.sendError(results.getHttpStatusCode());
        }
        IOUtils.copy(results.getResponse(), response.getOutputStream());
    }
