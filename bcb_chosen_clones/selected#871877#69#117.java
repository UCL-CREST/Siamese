    public int loadFromByteArray(byte[] array) {
        type = ByteUtils.b2i(array[0]);
        boolean[] flags = ByteUtils.booleansFromByte(array[1]);
        isConnClosed = flags[0];
        zipData = flags[1];
        encryptData = flags[2];
        boolean isThisPacketZipped = flags[3];
        boolean isThisPacketEncrypted = flags[4];
        int idLen = ByteUtils.b2i(array[2]);
        id = new String(array, 3, idLen);
        int dataLen = 256 * ByteUtils.b2i(array[3 + idLen]) + ByteUtils.b2i(array[4 + idLen]);
        byte[] workTab = new byte[dataLen];
        System.arraycopy(array, 5 + idLen, workTab, 0, dataLen);
        long crc = ByteUtils.longFromBytes(array[12 + idLen + dataLen], array[11 + idLen + dataLen], array[10 + idLen + dataLen], array[9 + idLen + dataLen], array[8 + idLen + dataLen], array[7 + idLen + dataLen], array[6 + idLen + dataLen], array[5 + idLen + dataLen]);
        int extLen = 256 * ByteUtils.b2i(array[13 + idLen + dataLen]) + ByteUtils.b2i(array[14 + idLen + dataLen]);
        byte[] extensions = new byte[extLen];
        System.arraycopy(array, 15 + idLen + dataLen, extensions, 0, extLen);
        byte nullByte = array[15 + idLen + dataLen + extLen];
        if (nullByte != 0) {
        }
        if (isThisPacketEncrypted) {
            try {
                workTab = ByteUtils.decryptRaw(encryptionKey, workTab);
            } catch (IOException e) {
                tab = workTab;
                errorCode = 3;
                return (3);
            }
        }
        if (isThisPacketZipped) {
            try {
                workTab = ByteUtils.unpackRaw(workTab);
            } catch (IOException ioe) {
                tab = workTab;
                errorCode = 2;
                return (2);
            }
        }
        tab = workTab;
        java.util.zip.CRC32 crcComputer = new java.util.zip.CRC32();
        crcComputer.update(tab);
        long computedCrc = crcComputer.getValue();
        if (computedCrc != crc) {
            errorCode = 1;
            return (1);
        }
        errorCode = 0;
        return (0);
    }
