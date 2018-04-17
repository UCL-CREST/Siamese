    private void cRC32Test(byte[] values, long expected) {
        CRC32 crc = new CRC32();
        crc.update(values);
        assertEquals(crc.getValue(), expected);
        crc.reset();
        for (int i = 0; i < values.length; i++) {
            crc.update(values[i]);
        }
        assertEquals(crc.getValue(), expected);
    }
