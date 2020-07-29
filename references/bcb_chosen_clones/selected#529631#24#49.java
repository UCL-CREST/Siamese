    public void execute(HttpServletRequest req, HttpServletResponse res, HttpServlet parent) throws Exception {
        String path = req.getParameter("path");
        path = URLDecoder.decode(path, "UTF-8");
        String name = req.getParameter("name");
        name = new String(name.getBytes(), "UTF-8");
        String contentType = req.getParameter("contentType");
        if (path == null) {
            NullPointerException e = new NullPointerException("The path attribute cannot be retrieved.");
            LOG.error(e);
            throw e;
        }
        URL url = new URL(path);
        InputStream inStream = null;
        try {
            inStream = URLUtils.getFileContent(url, req.getSession().getId());
            res.setContentType(contentType);
            res.addHeader("Content-Disposition", "attachment;filename=\"" + name + "\"");
            ServletOutputStream out = res.getOutputStream();
            IOUtils.copy(inStream, out);
            res.flushBuffer();
        } finally {
            if (inStream != null) {
                inStream.close();
            }
        }
    }
