    @Override
    public int hashCode() {
        int result = cachedHash;
        if (0 == result) {
            Checksum crc = new CRC32();
            crc.update(bytes, 0, bytes.length);
            cachedHash = (int) crc.getValue();
            cachedHash = (0 == cachedHash) ? 1 : cachedHash;
            result = cachedHash;
        }
        return result;
    }
