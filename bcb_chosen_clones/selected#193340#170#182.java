    public static String getCRC32(File file) throws IOException, NoSuchAlgorithmException {
        FileInputStream input = new FileInputStream(file);
        byte[] buffer = new byte[BUFFER_SIZE];
        CRC32 crc32digest = new CRC32();
        int i = 0;
        while ((i = input.read(buffer, 0, BUFFER_SIZE)) > 0) {
            crc32digest.update(buffer, 0, i);
        }
        input.close();
        String rawtext = "00000000" + Long.toHexString(crc32digest.getValue());
        String crc32 = rawtext.substring(rawtext.length() - 8);
        return crc32.toUpperCase();
    }
