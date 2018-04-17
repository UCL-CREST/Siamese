    public void updateCRC() {
        setCRC(0);
        int iCRC = 0;
        CRC32 crcAlg = new CRC32();
        crcAlg.update(toByteArray());
        iCRC = (int) crcAlg.getValue();
        setCRC(iCRC);
    }
