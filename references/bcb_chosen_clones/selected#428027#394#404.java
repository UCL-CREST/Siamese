    private static long copy(InputStream source, OutputStream sink) {
        try {
            return IOUtils.copyLarge(source, sink);
        } catch (IOException e) {
            logger.error(e.toString(), e);
            throw new FaultException("System error copying stream", e);
        } finally {
            IOUtils.closeQuietly(source);
            IOUtils.closeQuietly(sink);
        }
    }
