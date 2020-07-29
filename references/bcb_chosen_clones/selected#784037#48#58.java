    public void test_updateI() {
        CRC32 crc = new CRC32();
        crc.update(1);
        assertEquals("update(1) failed to update the checksum to the correct value ", 2768625435L, crc.getValue());
        crc.reset();
        crc.update(Integer.MAX_VALUE);
        assertEquals("update(max) failed to update the checksum to the correct value ", 4278190080L, crc.getValue());
        crc.reset();
        crc.update(Integer.MIN_VALUE);
        assertEquals("update(min) failed to update the checksum to the correct value ", 3523407757L, crc.getValue());
    }
