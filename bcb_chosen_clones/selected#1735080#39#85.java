    @Override
    public int onPut(Operation operation) {
        synchronized (MuleObexRequestHandler.connections) {
            MuleObexRequestHandler.connections++;
            if (logger.isDebugEnabled()) {
                logger.debug("Connection accepted, total number of connections: " + MuleObexRequestHandler.connections);
            }
        }
        int result = ResponseCodes.OBEX_HTTP_OK;
        try {
            headers = operation.getReceivedHeaders();
            if (!this.maxFileSize.equals(ObexServer.UNLIMMITED_FILE_SIZE)) {
                Long fileSize = (Long) headers.getHeader(HeaderSet.LENGTH);
                if (fileSize == null) {
                    result = ResponseCodes.OBEX_HTTP_LENGTH_REQUIRED;
                }
                if (fileSize > this.maxFileSize) {
                    result = ResponseCodes.OBEX_HTTP_REQ_TOO_LARGE;
                }
            }
            if (result != ResponseCodes.OBEX_HTTP_OK) {
                InputStream in = operation.openInputStream();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                IOUtils.copy(in, out);
                in.close();
                out.close();
                data = out.toByteArray();
                if (interrupted) {
                    data = null;
                    result = ResponseCodes.OBEX_HTTP_GONE;
                }
            }
            return result;
        } catch (IOException e) {
            return ResponseCodes.OBEX_HTTP_UNAVAILABLE;
        } finally {
            synchronized (this) {
                this.notify();
            }
            synchronized (MuleObexRequestHandler.connections) {
                MuleObexRequestHandler.connections--;
                if (logger.isDebugEnabled()) {
                    logger.debug("Connection closed, total number of connections: " + MuleObexRequestHandler.connections);
                }
            }
        }
    }
