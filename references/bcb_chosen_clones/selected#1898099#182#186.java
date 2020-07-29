    public int calculateBackupEntriesChecksum() {
        CRC32 checksum = new CRC32();
        for (GPTEntry entry : backupEntries) checksum.update(entry.getBytes());
        return (int) (checksum.getValue() & 0xFFFFFFFF);
    }
