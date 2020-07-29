    protected void doProxyInternally(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpRequestBase proxyReq = buildProxyRequest(req);
        URI reqUri = proxyReq.getURI();
        String cookieDomain = reqUri.getHost();
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpContext httpContext = new BasicHttpContext();
        httpContext.setAttribute("org.atricorel.idbus.kernel.main.binding.http.HttpServletRequest", req);
        int intIdx = 0;
        for (int i = 0; i < httpClient.getRequestInterceptorCount(); i++) {
            if (httpClient.getRequestInterceptor(i) instanceof RequestAddCookies) {
                intIdx = i;
                break;
            }
        }
        IDBusRequestAddCookies interceptor = new IDBusRequestAddCookies(cookieDomain);
        httpClient.removeRequestInterceptorByClass(RequestAddCookies.class);
        httpClient.addRequestInterceptor(interceptor, intIdx);
        httpClient.getParams().setParameter(ClientPNames.HANDLE_REDIRECTS, false);
        httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
        if (logger.isTraceEnabled()) logger.trace("Staring to follow redirects for " + req.getPathInfo());
        HttpResponse proxyRes = null;
        List<Header> storedHeaders = new ArrayList<Header>(40);
        boolean followTargetUrl = true;
        byte[] buff = new byte[1024];
        while (followTargetUrl) {
            if (logger.isTraceEnabled()) logger.trace("Sending internal request " + proxyReq);
            proxyRes = httpClient.execute(proxyReq, httpContext);
            String targetUrl = null;
            Header[] headers = proxyRes.getAllHeaders();
            for (Header header : headers) {
                if (header.getName().equals("Server")) continue;
                if (header.getName().equals("Transfer-Encoding")) continue;
                if (header.getName().equals("Location")) continue;
                if (header.getName().equals("Expires")) continue;
                if (header.getName().equals("Content-Length")) continue;
                if (header.getName().equals("Content-Type")) continue;
                storedHeaders.add(header);
            }
            if (logger.isTraceEnabled()) logger.trace("HTTP/STATUS:" + proxyRes.getStatusLine().getStatusCode() + "[" + proxyReq + "]");
            switch(proxyRes.getStatusLine().getStatusCode()) {
                case 200:
                    followTargetUrl = false;
                    break;
                case 404:
                    followTargetUrl = false;
                    break;
                case 500:
                    followTargetUrl = false;
                    break;
                case 302:
                    Header location = proxyRes.getFirstHeader("Location");
                    targetUrl = location.getValue();
                    if (!internalProcessingPolicy.match(req, targetUrl)) {
                        if (logger.isTraceEnabled()) logger.trace("Do not follow HTTP 302 to [" + location.getValue() + "]");
                        Collections.addAll(storedHeaders, proxyRes.getHeaders("Location"));
                        followTargetUrl = false;
                    } else {
                        if (logger.isTraceEnabled()) logger.trace("Do follow HTTP 302 to [" + location.getValue() + "]");
                        followTargetUrl = true;
                    }
                    break;
                default:
                    followTargetUrl = false;
                    break;
            }
            HttpEntity entity = proxyRes.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                try {
                    if (!followTargetUrl) {
                        for (Header header : headers) {
                            if (header.getName().equals("Content-Type")) res.setHeader(header.getName(), header.getValue());
                            if (header.getName().equals("Content-Length")) res.setHeader(header.getName(), header.getValue());
                        }
                        res.setStatus(proxyRes.getStatusLine().getStatusCode());
                        for (Header header : storedHeaders) {
                            if (header.getName().startsWith("Set-Cookie")) res.addHeader(header.getName(), header.getValue()); else res.setHeader(header.getName(), header.getValue());
                        }
                        IOUtils.copy(instream, res.getOutputStream());
                        res.getOutputStream().flush();
                    } else {
                        int r = instream.read(buff);
                        int total = r;
                        while (r > 0) {
                            r = instream.read(buff);
                            total += r;
                        }
                        if (total > 0) logger.warn("Ignoring response content size : " + total);
                    }
                } catch (IOException ex) {
                    throw ex;
                } catch (RuntimeException ex) {
                    proxyReq.abort();
                    throw ex;
                } finally {
                    try {
                        instream.close();
                    } catch (Exception ignore) {
                    }
                }
            } else {
                if (!followTargetUrl) {
                    res.setStatus(proxyRes.getStatusLine().getStatusCode());
                    for (Header header : headers) {
                        if (header.getName().equals("Content-Type")) res.setHeader(header.getName(), header.getValue());
                        if (header.getName().equals("Content-Length")) res.setHeader(header.getName(), header.getValue());
                    }
                    for (Header header : storedHeaders) {
                        if (header.getName().startsWith("Set-Cookie")) res.addHeader(header.getName(), header.getValue()); else res.setHeader(header.getName(), header.getValue());
                    }
                }
            }
            if (followTargetUrl) {
                proxyReq = buildProxyRequest(targetUrl);
                httpContext = null;
            }
        }
        if (logger.isTraceEnabled()) logger.trace("Ended following redirects for " + req.getPathInfo());
    }
