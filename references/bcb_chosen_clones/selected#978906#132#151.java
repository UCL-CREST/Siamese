    private void removeSessionId(InputStream inputStream, Output output) throws IOException {
        String jsessionid = RewriteUtils.getSessionId(target);
        boolean textContentType = ResourceUtils.isTextContentType(httpClientResponse.getHeader(HttpHeaders.CONTENT_TYPE), target.getDriver().getConfiguration().getParsableContentTypes());
        if (jsessionid == null || !textContentType) {
            IOUtils.copy(inputStream, output.getOutputStream());
        } else {
            String charset = httpClientResponse.getContentCharset();
            if (charset == null) {
                charset = "ISO-8859-1";
            }
            String content = IOUtils.toString(inputStream, charset);
            content = removeSessionId(jsessionid, content);
            if (output.getHeader(HttpHeaders.CONTENT_LENGTH) != null) {
                output.setHeader(HttpHeaders.CONTENT_LENGTH, Integer.toString(content.length()));
            }
            OutputStream outputStream = output.getOutputStream();
            IOUtils.write(content, outputStream, charset);
        }
        inputStream.close();
    }
