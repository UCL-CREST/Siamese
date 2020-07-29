    public static void main(String[] args) {
        CRC32 crc = new CRC32();
        crc.update("/path/file.html".getBytes());
        long val = crc.getValue();
        System.out.println(Long.toHexString(val));
    }
