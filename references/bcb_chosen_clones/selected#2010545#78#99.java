    private void delay(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String url = request.getRequestURL().toString();
        if (delayed.contains(url)) {
            delayed.remove(url);
            LOGGER.info(MessageFormat.format("Loading delayed resource at url = [{0}]", url));
            chain.doFilter(request, response);
        } else {
            LOGGER.info("Returning resource = [LoaderApplication.swf]");
            InputStream input = null;
            OutputStream output = null;
            try {
                input = getClass().getResourceAsStream("LoaderApplication.swf");
                output = response.getOutputStream();
                delayed.add(url);
                response.setHeader("Cache-Control", "no-cache");
                IOUtils.copy(input, output);
            } finally {
                IOUtils.closeQuietly(output);
                IOUtils.closeQuietly(input);
            }
        }
    }
