    public static long getFileChecksum(final String fileName) throws IOException {
        File file = new File(fileName);
        byte[] buffer = new byte[(4 << 10)];
        CRC32 checksum;
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            checksum = new CRC32();
            checksum.reset();
            int bytesRead;
            while ((bytesRead = in.read(buffer)) >= 0) {
                checksum.update(buffer, 0, bytesRead);
            }
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return checksum.getValue();
    }
