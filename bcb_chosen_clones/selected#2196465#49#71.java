    public static byte[] zipBytes(byte[] bytes, String aName) throws IOException {
        ByteArrayOutputStream tempOStream = null;
        BufferedOutputStream tempBOStream = null;
        ZipOutputStream tempZStream = null;
        ZipEntry tempEntry = null;
        byte[] tempBytes = null;
        CRC32 tempCRC = null;
        tempOStream = new ByteArrayOutputStream(bytes.length);
        tempBOStream = new BufferedOutputStream(tempOStream);
        tempZStream = new ZipOutputStream(tempBOStream);
        tempCRC = new CRC32();
        tempCRC.update(bytes, 0, bytes.length);
        tempEntry = new ZipEntry(aName);
        tempEntry.setMethod(ZipEntry.STORED);
        tempEntry.setSize(bytes.length);
        tempEntry.setCrc(tempCRC.getValue());
        tempZStream.putNextEntry(tempEntry);
        tempZStream.write(bytes, 0, bytes.length);
        tempZStream.flush();
        tempBytes = tempOStream.toByteArray();
        tempZStream.close();
        return tempBytes;
    }
