    protected void renderResource(final HttpServletRequest request, final HttpServletResponse response, final InputStream is) throws IOException {
        try {
            IOUtils.copy(is, response.getOutputStream());
        } finally {
            IOUtils.closeQuietly(is);
        }
    }
