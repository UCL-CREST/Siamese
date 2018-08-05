    @Override
    protected void sendQuietly(HttpServletResponse response) throws Exception {
        IOUtils.copy(new CharArrayReader(responseWrapper.getContents()), response.getOutputStream(), getEncoding());
    }
