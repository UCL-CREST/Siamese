    public static int proxy(java.net.URI uri, HttpServletRequest req, HttpServletResponse res) throws IOException {
        final HostConfiguration hostConfig = new HostConfiguration();
        hostConfig.setHost(uri.getHost());
        HttpMethodBase httpMethod = null;
        if (HttpRpcServer.METHOD_GET.equalsIgnoreCase(req.getMethod())) {
            httpMethod = new GetMethod(uri.toString());
            httpMethod.setFollowRedirects(true);
        } else if (HttpRpcServer.METHOD_POST.equalsIgnoreCase(req.getMethod())) {
            httpMethod = new PostMethod(uri.toString());
            final Enumeration parameterNames = req.getParameterNames();
            if (parameterNames != null) while (parameterNames.hasMoreElements()) {
                final String parameterName = (String) parameterNames.nextElement();
                for (String parameterValue : req.getParameterValues(parameterName)) ((PostMethod) httpMethod).addParameter(parameterName, parameterValue);
            }
            ((PostMethod) httpMethod).setRequestEntity(new InputStreamRequestEntity(req.getInputStream()));
        }
        if (httpMethod == null) throw new IllegalArgumentException("Unsupported http request method");
        final int responseCode;
        final Enumeration headers = req.getHeaderNames();
        if (headers != null) while (headers.hasMoreElements()) {
            final String headerName = (String) headers.nextElement();
            final Enumeration headerValues = req.getHeaders(headerName);
            while (headerValues.hasMoreElements()) {
                httpMethod.setRequestHeader(headerName, (String) headerValues.nextElement());
            }
        }
        final HttpState httpState = new HttpState();
        if (req.getCookies() != null) for (Cookie cookie : req.getCookies()) {
            String host = req.getHeader("Host");
            if (StringUtils.isEmpty(cookie.getDomain())) cookie.setDomain(StringUtils.isEmpty(host) ? req.getServerName() + ":" + req.getServerPort() : host);
            if (StringUtils.isEmpty(cookie.getPath())) cookie.setPath("/");
            httpState.addCookie(new org.apache.commons.httpclient.Cookie(cookie.getDomain(), cookie.getName(), cookie.getValue(), cookie.getPath(), cookie.getMaxAge(), cookie.getSecure()));
        }
        httpMethod.setQueryString(req.getQueryString());
        responseCode = (new HttpClient()).executeMethod(hostConfig, httpMethod, httpState);
        if (responseCode < 0) {
            httpMethod.releaseConnection();
            return responseCode;
        }
        if (httpMethod.getResponseHeaders() != null) for (Header header : httpMethod.getResponseHeaders()) res.setHeader(header.getName(), header.getValue());
        final InputStream in = httpMethod.getResponseBodyAsStream();
        final OutputStream out = res.getOutputStream();
        IOUtils.copy(in, out);
        out.flush();
        out.close();
        in.close();
        httpMethod.releaseConnection();
        return responseCode;
    }
