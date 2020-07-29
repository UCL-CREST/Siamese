    public static String getCRC32(String iString) {
        String out = null;
        System.out.println("About to compute CRC32 upon the string : <" + iString + ">");
        byte[] bytes = iString.getBytes();
        CRC32 crc = new CRC32();
        crc.update(bytes);
        out = Long.toHexString(crc.getValue()).toUpperCase();
        System.out.println("Computed CRC32 returns : " + out);
        System.out.println("Bye.");
        return out;
    }
