    public void render(final HttpServletRequest request, final HttpServletResponse response, final byte[] bytes, final Throwable t, final String contentType, final String encoding) throws Exception {
        if (contentType != null) {
            response.setContentType(contentType);
        }
        if (encoding != null) {
            response.setCharacterEncoding(encoding);
        }
        response.setContentLength(bytes.length);
        IOUtils.copy(new ByteArrayInputStream(bytes), response.getOutputStream());
    }
