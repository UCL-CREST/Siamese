    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        String pluginPathInfo = pathInfo.substring(prefix.length());
        String gwtPathInfo = pluginPathInfo.substring(pluginKey.length() + 1);
        String clPath = CLASSPATH_PREFIX + gwtPathInfo;
        InputStream input = cl.getResourceAsStream(clPath);
        if (input != null) {
            try {
                OutputStream output = resp.getOutputStream();
                IOUtils.copy(input, output);
            } finally {
                input.close();
            }
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
