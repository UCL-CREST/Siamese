    @Override
    public synchronized int hashCode() {
        Checksum crc = new CRC32();
        crc.update(b, offset, len);
        int dataHash = (int) crc.getValue();
        int result = super.hashCode() * 6037 + dataHash;
        return (0 != result) ? result : 1;
    }
