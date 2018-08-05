    protected void saveResponse(final WebResponse response, final WebRequest request) throws IOException {
        counter_++;
        final String extension = chooseExtension(response.getContentType());
        final File f = createFile(request.getUrl(), extension);
        final InputStream input = response.getContentAsStream();
        final OutputStream output = new FileOutputStream(f);
        try {
            IOUtils.copy(response.getContentAsStream(), output);
        } finally {
            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(output);
        }
        final URL url = response.getWebRequest().getUrl();
        LOG.info("Created file " + f.getAbsolutePath() + " for response " + counter_ + ": " + url);
        final StringBuilder buffer = new StringBuilder();
        buffer.append("tab[tab.length] = {code: " + response.getStatusCode() + ", ");
        buffer.append("fileName: '" + f.getName() + "', ");
        buffer.append("contentType: '" + response.getContentType() + "', ");
        buffer.append("method: '" + request.getHttpMethod().name() + "', ");
        if (request.getHttpMethod() == HttpMethod.POST && request.getEncodingType() == FormEncodingType.URL_ENCODED) {
            buffer.append("postParameters: " + nameValueListToJsMap(request.getRequestParameters()) + ", ");
        }
        buffer.append("url: '" + escapeJSString(url.toString()) + "', ");
        buffer.append("loadTime: " + response.getLoadTime() + ", ");
        final byte[] bytes = IOUtils.toByteArray(response.getContentAsStream());
        buffer.append("responseSize: " + ((bytes == null) ? 0 : bytes.length) + ", ");
        buffer.append("responseHeaders: " + nameValueListToJsMap(response.getResponseHeaders()));
        buffer.append("};\n");
        appendToJSFile(buffer.toString());
    }
