    public String xcrc(String filename, long start, long end) throws IOException {
        File file = resolveFile(filename);
        if (!file.exists() && filename.startsWith("\"") && filename.endsWith("\"")) {
            file = resolveFile(filename.substring(1, filename.length() - 1));
        }
        CRC32 crc = new CRC32();
        FileInputStream in = null;
        try {
            byte[] buf = new byte[8192];
            int len;
            if (start == 0 && end == 0) {
                in = new FileInputStream(file);
            } else {
                in = new LimitedFileInputStream(file, start, end);
            }
            while ((len = in.read(buf, 0, buf.length)) != -1) {
                crc.update(buf, 0, len);
            }
            return Long.toHexString(crc.getValue()).toUpperCase();
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }
