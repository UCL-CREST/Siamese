    protected boolean doRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        if (!path.startsWith(alias)) {
            throw new ServletException("Path '" + path + "' does not start with registered alias '" + alias + "'");
        }
        String internal;
        if (alias.equals("/")) {
            internal = name + path;
        } else {
            internal = name + path.substring(alias.length(), path.length());
        }
        URL resource = httpContext.getResource(internal);
        if (resource == null) {
            return false;
        }
        String mimeType = servletContext.getMimeType(internal);
        if (mimeType != null) {
            response.setContentType(mimeType);
        }
        InputStream is = resource.openStream();
        OutputStream os = response.getOutputStream();
        IOUtils.copyAndClose(is, os);
        return true;
    }
