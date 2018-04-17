    public void render(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(s_contentType);
        response.setHeader("Cache-control", "no-cache");
        InputStream graphStream = getGraphStream(request);
        OutputStream out = getOutputStream(response);
        IOUtils.copy(graphStream, out);
        out.flush();
    }
