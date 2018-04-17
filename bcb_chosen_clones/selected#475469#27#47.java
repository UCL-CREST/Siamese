    public long getChecksumValue(File fname) throws Exception {
        Checksum checksum = new CRC32();
        checksum.reset();
        BufferedInputStream inputStream = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(fname));
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(bytes)) >= 0) {
                checksum.update(bytes, 0, len);
            }
        } catch (Exception ex) {
            return -1;
        } finally {
            try {
                inputStream.close();
            } catch (Exception ex) {
            }
        }
        return checksum.getValue();
    }
