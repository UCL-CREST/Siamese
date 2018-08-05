    private static ByteBuffer calculateCrcTable() throws IOException {
        final CRC32 crc = new CRC32();
        int[] checksums = new int[9];
        checksums[0] = Server.VERSION;
        for (int i = 1; i < checksums.length; i++) {
            byte[] file = cache.getFile(0, i).getBytes();
            crc.reset();
            crc.update(file, 0, file.length);
            checksums[i] = (int) crc.getValue();
        }
        int hash = 1234;
        for (int i = 0; i < checksums.length; i++) {
            hash = (hash << 1) + checksums[i];
        }
        ByteBuffer bb = ByteBuffer.allocate(4 * (checksums.length + 1));
        for (int i = 0; i < checksums.length; i++) {
            bb.putInt(checksums[i]);
        }
        bb.putInt(hash);
        bb.flip();
        return bb;
    }
