    public void test_reset() {
        CRC32 crc = new CRC32();
        crc.update(1);
        assertEquals("update(int) failed to update the checksum to the correct value ", 2768625435L, crc.getValue());
        crc.reset();
        assertEquals("reset failed to reset the checksum value to zero", 0, crc.getValue());
    }
