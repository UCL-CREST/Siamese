    private long getFileCRC32(File f) throws IOException {
        if (f.exists() && f.isFile()) {
            FileInputStream fis = new FileInputStream(f);
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
