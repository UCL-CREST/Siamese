    public void sendContent(OutputStream out, Range range, Map<String, String> params, String contentType) throws IOException {
        LOGGER.debug("GET REQUEST OR RESPONSE - Send content: " + file.getAbsolutePath());
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            int bytes = IOUtils.copy(in, out);
            LOGGER.debug("wrote bytes:  " + bytes);
            out.flush();
        } finally {
            IOUtils.closeQuietly(in);
        }
    }
