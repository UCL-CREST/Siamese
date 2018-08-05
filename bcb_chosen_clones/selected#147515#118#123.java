    private String getRequestMessage(InputStream in) throws IOException, UnsupportedEncodingException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(INITIAL_BUFFER_SIZE);
        IOUtils.copyStream(baos, in);
        String reqMessage = new String(baos.toByteArray(), charsetEncoding);
        return reqMessage;
    }
