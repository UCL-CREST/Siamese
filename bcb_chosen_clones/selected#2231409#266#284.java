    private int computeCRC(ByteData bytes) throws IOException {
        final CRC32 crc = new CRC32();
        OutputStream out = new OutputStream() {

            public void write(int b) {
                crc.update(b);
            }

            public void write(byte[] b) {
                crc.update(b, 0, b.length);
            }

            public void write(byte[] b, int off, int len) {
                crc.update(b, off, len);
            }
        };
        bytes.writeTo(out);
        return (int) crc.getValue();
    }
