    public long computeCRC32() throws IOException {
        CRC32 compCRC32 = new CRC32();
        for (int i = 0; i < numDataRecords; i++) {
            try {
                compCRC32.update(readRecord(i + 1));
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new IOException("computeCRC32: Index out of bounds reading record " + (i + 1));
            } catch (IOException e) {
                throw new IOException("computeCRC32: error reading record " + (i + 1));
            }
        }
        return compCRC32.getValue();
    }
