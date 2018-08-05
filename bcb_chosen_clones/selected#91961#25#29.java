    public String getFile(HttpServletRequest request, OutputStream outputStream) throws IOException {
        InputStream inputStream = request.getInputStream();
        org.apache.commons.io.IOUtils.copy(inputStream, outputStream);
        return null;
    }
