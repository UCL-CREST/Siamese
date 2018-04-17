    public void render(HttpServletRequest request, HttpServletResponse response, File file, final Throwable t, final String contentType, final String encoding) throws Exception {
        if (contentType != null) {
            response.setContentType(contentType);
        }
        if (encoding != null) {
            response.setCharacterEncoding(encoding);
        }
        if (file.length() > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Cannot send file greater than 2GB");
        }
        response.setContentLength((int) file.length());
        IOUtils.copy(new FileInputStream(file), response.getOutputStream());
    }
