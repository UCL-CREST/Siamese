    public void test_update$B() {
        byte byteArray[] = { 1, 2 };
        CRC32 crc = new CRC32();
        crc.update(byteArray);
        assertEquals("update(byte[]) failed to update the checksum to the correct value ", 3066839698L, crc.getValue());
        crc.reset();
        byte byteEmpty[] = new byte[10000];
        crc.update(byteEmpty);
        assertEquals("update(byte[]) failed to update the checksum to the correct value ", 1295764014L, crc.getValue());
    }
