    protected void serveStaticContent(HttpServletRequest request, HttpServletResponse response, String pathInfo) throws ServletException {
        InputStream is = servletConfig.getServletContext().getResourceAsStream(pathInfo);
        if (is == null) {
            throw new ServletException("Static resource " + pathInfo + " is not available");
        }
        try {
            int ind = pathInfo.lastIndexOf(".");
            if (ind != -1 && ind < pathInfo.length()) {
                String type = STATIC_CONTENT_TYPES.get(pathInfo.substring(ind + 1));
                if (type != null) {
                    response.setContentType(type);
                }
            }
            ServletOutputStream os = response.getOutputStream();
            IOUtils.copy(is, os);
            os.flush();
        } catch (IOException ex) {
            throw new ServletException("Static resource " + pathInfo + " can not be written to the output stream");
        }
    }
