    public static long getCRC(String content) {
        if (content.length() == 0) return 0;
        final java.util.zip.CRC32 crc = new java.util.zip.CRC32();
        java.io.OutputStream crcOut = new java.io.OutputStream() {

            @Override
            public void write(int b) throws java.io.IOException {
                crc.update(b);
            }
        };
        try {
            new java.io.OutputStreamWriter(crcOut).write(content);
        } catch (java.io.IOException e) {
            throw new IllegalStateException("Could not check CRC of message");
        }
        return crc.getValue();
    }
