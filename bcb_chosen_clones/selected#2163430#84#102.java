    public Long createChecksumLong() throws IOException {
        Long ret = 0L;
        try {
            InputStream in = new FileInputStream(file);
            CRC32 checksum = new CRC32();
            checksum.reset();
            byte[] buffer = new byte[BUFFERSIZE];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) >= 0) {
                checksum.update(buffer, 0, bytesRead);
            }
            ret = checksum.getValue();
            in.close();
        } catch (IOException e) {
            System.out.println("Couldn't read file or file name (corrupted?) : " + file.getAbsolutePath());
            ret = 0L;
        }
        return ret;
    }
