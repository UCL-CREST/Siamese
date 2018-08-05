    public static Long createCRC32Checksum(File file) throws IOException {
        logger.debug("createCRC32Checksum(" + file.toString() + ")");
        InputStream inputStream = new FileInputStream(file);
        CRC32 checksum = new CRC32();
        checksum.reset();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) >= 0) {
            checksum.update(buffer, 0, bytesRead);
        }
        inputStream.close();
        logger.debug("CRC32 Checksum value: " + checksum.getValue());
        return new Long(checksum.getValue());
    }
