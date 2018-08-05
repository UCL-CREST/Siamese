    public static Long createChecksum(File file) throws IOException {
        long millis = System.currentTimeMillis();
        InputStream in = openFileStream(file);
        CRC32 checksum = new CRC32();
        checksum.reset();
        byte[] buffer = new byte[bufferSize];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) >= 0) {
            checksum.update(buffer, 0, bytesRead);
        }
        in.close();
        if (clock) {
            millis = System.currentTimeMillis() - millis;
        }
        return Long.valueOf(checksum.getValue());
    }
