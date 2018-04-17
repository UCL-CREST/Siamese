    private void doPOST(HttpURLConnection connection, InputStream inputXML) throws MessageServiceException {
        try {
            OutputStream requestStream = new BufferedOutputStream(connection.getOutputStream());
            IOUtils.copyAndClose(inputXML, requestStream);
            connection.connect();
        } catch (IOException e) {
            throw new MessageServiceException(e.getMessage(), e);
        }
    }
