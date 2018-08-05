    public byte[] saveToByteArray() {
        boolean isThisPacketZipped = false;
        boolean isThisPacketEncrypted = false;
        byte[] workTab = tab;
        if (zipData) {
            try {
                byte[] zipTab = ByteUtils.packRaw(workTab);
                if (zipTab.length < workTab.length) {
                    workTab = zipTab;
                    isThisPacketZipped = true;
                } else isThisPacketZipped = false;
            } catch (IOException ioe) {
                isThisPacketZipped = false;
            }
        }
        if (encryptData) {
            try {
                workTab = ByteUtils.encryptRaw(encryptionKey, workTab);
                isThisPacketEncrypted = true;
            } catch (IOException e) {
                isThisPacketEncrypted = false;
            }
        }
        byte[] extensions = new byte[0];
        byte[] array = new byte[1 + 1 + 1 + id.length() + 2 + workTab.length + 8 + 2 + extensions.length + 1];
        array[0] = ByteUtils.i2b(type);
        array[1] = ByteUtils.byteFromBooleans(false, false, false, isThisPacketEncrypted, isThisPacketZipped, encryptData, zipData, isConnClosed);
        array[2] = ByteUtils.i2b(id.length());
        byte[] tabId = id.getBytes();
        System.arraycopy(tabId, 0, array, 3, tabId.length);
        array[tabId.length + 3] = ByteUtils.i2b(workTab.length / 256);
        array[tabId.length + 4] = ByteUtils.i2b(workTab.length % 256);
        System.arraycopy(workTab, 0, array, tabId.length + 5, workTab.length);
        java.util.zip.CRC32 crcComputer = new java.util.zip.CRC32();
        crcComputer.update(tab);
        long crc = crcComputer.getValue();
        byte[] bCrc = ByteUtils.bytesFromLong(crc);
        System.arraycopy(bCrc, 0, array, tabId.length + workTab.length + 5, 8);
        array[tabId.length + workTab.length + 13] = ByteUtils.i2b(extensions.length / 256);
        array[tabId.length + workTab.length + 14] = ByteUtils.i2b(extensions.length % 256);
        System.arraycopy(extensions, 0, array, tabId.length + workTab.length + 15, extensions.length);
        array[tabId.length + workTab.length + extensions.length + 15] = 0;
        return (array);
    }
