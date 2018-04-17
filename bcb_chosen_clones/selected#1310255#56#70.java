    public static long getFileCRC32(File file) throws IOException {
        if (file.exists() && file.isFile()) {
            FileInputStream fis = new FileInputStream(file);
            CRC32 check = new CRC32();
            int b = fis.read();
            while (b != -1) {
                b = fis.read();
                check.update(b);
            }
            fis.close();
            return check.getValue();
        } else {
            return 0;
        }
    }
