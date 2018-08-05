    public static long getCheckSum(String chaine) {
        CRC32 crc32 = new CRC32();
        crc32.update(chaine.getBytes());
        return crc32.getValue();
    }
