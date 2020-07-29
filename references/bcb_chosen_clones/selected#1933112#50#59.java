    private void writeMessage(ChannelBuffer buffer, File dst) throws IOException {
        ChannelBufferInputStream is = new ChannelBufferInputStream(buffer);
        OutputStream os = null;
        try {
            os = new FileOutputStream(dst);
            IOUtils.copyLarge(is, os);
        } finally {
            IOUtils.closeQuietly(os);
        }
    }
