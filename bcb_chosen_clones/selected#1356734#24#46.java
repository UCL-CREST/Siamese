    public TableDirectory(RandomAccessFile raf) throws IOException {
        version = raf.readInt();
        numTables = raf.readShort();
        searchRange = raf.readShort();
        entrySelector = raf.readShort();
        rangeShift = raf.readShort();
        entries = new DirectoryEntry[numTables];
        for (int i = 0; i < numTables; i++) {
            entries[i] = new DirectoryEntry(raf);
        }
        boolean modified = true;
        while (modified) {
            modified = false;
            for (int i = 0; i < numTables - 1; i++) {
                if (entries[i].getOffset() > entries[i + 1].getOffset()) {
                    DirectoryEntry temp = entries[i];
                    entries[i] = entries[i + 1];
                    entries[i + 1] = temp;
                    modified = true;
                }
            }
        }
    }
