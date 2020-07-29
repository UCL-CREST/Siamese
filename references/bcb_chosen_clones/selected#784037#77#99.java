    public void test_update$BII() {
        byte[] byteArray = { 1, 2, 3 };
        CRC32 crc = new CRC32();
        int off = 2;
        int len = 1;
        int lenError = 3;
        int offError = 4;
        crc.update(byteArray, off, len);
        assertEquals("update(byte[],int,int) failed to update the checksum to the correct value ", 1259060791L, crc.getValue());
        int r = 0;
        try {
            crc.update(byteArray, off, lenError);
        } catch (ArrayIndexOutOfBoundsException e) {
            r = 1;
        }
        assertEquals("update(byte[],int,int) failed b/c lenError>byte[].length-off", 1, r);
        try {
            crc.update(byteArray, offError, len);
        } catch (ArrayIndexOutOfBoundsException e) {
            r = 2;
        }
        assertEquals("update(byte[],int,int) failed b/c offError>byte[].length", 2, r);
    }
