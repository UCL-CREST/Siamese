    public static long GetLocalFileCrc32(File file) throws IOException {
        CRC32 crc32 = new CRC32();
        BufferedInputStream bis = null;
        if (file.isFile()) {
            int byteCount;
            byte[] buffer = new byte[65536];
            try {
                bis = new BufferedInputStream(new FileInputStream(file));
                while (true) {
                    byteCount = bis.read(buffer);
                    if (byteCount == -1) {
                        break;
                    } else if (byteCount > 0) {
                        crc32.update(buffer, 0, byteCount);
                    }
                }
            } finally {
                try {
                    bis.close();
                } catch (IOException e) {
                }
            }
        } else {
            throw new IOException(file + " file not found");
        }
        return crc32.getValue();
    }
