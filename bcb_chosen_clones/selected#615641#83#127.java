    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
            final HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            final HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            final String contextPath = httpServletRequest.getContextPath() + "/";
            final String requestURI = httpServletRequest.getRequestURI();
            if (requestURI.startsWith(contextPath)) {
                final String filterURI = requestURI.substring(contextPath.length());
                final String filterPath = getFilterPath();
                if (filterURI.startsWith(filterPath)) {
                    final String targetURI = filterURI.substring(filterPath.length());
                    for (final Entry<String, String> entry : mapping.entrySet()) {
                        final String key = entry.getKey();
                        final String value = entry.getValue();
                        if (targetURI.startsWith(key) && (targetURI.length() > key.length())) {
                            final String resourceName = value + targetURI.substring(key.length());
                            InputStream is = null;
                            try {
                                is = getClass().getResourceAsStream("/" + resourceName);
                                if (is != null) {
                                    IOUtils.copy(is, httpServletResponse.getOutputStream());
                                    httpServletResponse.flushBuffer();
                                    break;
                                } else {
                                    httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
                                    break;
                                }
                            } catch (final IOException ioex) {
                                throw new ServletException("Error serving resource [" + resourceName + "].", ioex);
                            } finally {
                                IOUtils.closeQuietly(is);
                            }
                        }
                    }
                } else {
                    chain.doFilter(request, response);
                }
            } else {
                chain.doFilter(request, response);
            }
        } else {
            chain.doFilter(request, response);
        }
    }
