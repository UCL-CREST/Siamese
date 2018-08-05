    public void render(final HttpServletRequest request, final HttpServletResponse response, InputStream inputStream, final Throwable t, final String contentType, final String encoding) throws Exception {
        if (contentType != null) {
            response.setContentType(contentType);
        }
        if (encoding != null) {
            response.setCharacterEncoding(encoding);
        }
        IOUtils.copy(inputStream, response.getOutputStream());
    }
