    private static String getUnicodeStringIfOriginalMatches(AbstractUnicodeExtraField f, byte[] orig) {
        if (f != null) {
            CRC32 crc32 = new CRC32();
            crc32.update(orig);
            long origCRC32 = crc32.getValue();
            if (origCRC32 == f.getNameCRC32()) {
                try {
                    return ZipEncodingHelper.UTF8_ZIP_ENCODING.decode(f.getUnicodeName());
                } catch (IOException ex) {
                    return null;
                }
            }
        }
        return null;
    }
