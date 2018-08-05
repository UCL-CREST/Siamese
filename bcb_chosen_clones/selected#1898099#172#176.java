    public int calculatePrimaryEntriesChecksum() {
        CRC32 checksum = new CRC32();
        for (GPTEntry entry : entries) checksum.update(entry.getBytes());
        return (int) (checksum.getValue() & 0xFFFFFFFF);
    }
