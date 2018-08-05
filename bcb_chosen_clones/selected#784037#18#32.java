    public void test_getValue() {
        CRC32 crc = new CRC32();
        assertEquals("getValue() should return a zero as a result of constructing a CRC32 instance", 0, crc.getValue());
        crc.reset();
        crc.update(Integer.MAX_VALUE);
        assertEquals("update(max) failed to update the checksum to the correct value ", 4278190080L, crc.getValue());
        crc.reset();
        byte byteEmpty[] = new byte[10000];
        crc.update(byteEmpty);
        assertEquals("update(byte[]) failed to update the checksum to the correct value ", 1295764014L, crc.getValue());
        crc.reset();
        crc.update(1);
        crc.reset();
        assertEquals("reset failed to reset the checksum value to zero", 0, crc.getValue());
    }
