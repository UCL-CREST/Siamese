    private static synchronized String getId(XenaInputSource xis) {
        long idNumber = 0L;
        String systemID = xis.getSystemId();
        Date currentDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(TIMESTAMP_FORMAT_STRING);
        String timestampString = formatter.format(currentDate);
        String filename = SourceURIParser.getFileNameComponent(xis);
        CRC32 crc32 = new CRC32();
        crc32.update(systemID.getBytes());
        idNumber = crc32.getValue();
        String crcStr = Long.toHexString(idNumber);
        while (crcStr.length() < 8) {
            crcStr = "0" + crcStr;
        }
        return timestampString + "-" + filename + "-" + crcStr;
    }
