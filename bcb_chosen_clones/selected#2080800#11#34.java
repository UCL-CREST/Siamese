    public static String checkSum(File src) {
        try {
            Adler32 adler = new Adler32();
            CRC32 crc = new CRC32();
            long bytes = 0;
            FileInputStream in = new FileInputStream(src);
            byte[] buf = new byte[8000];
            int n;
            copying: while (true) {
                n = in.read(buf);
                if (n <= 0) {
                    break copying;
                }
                adler.update(buf, 0, n);
                crc.update(buf, 0, n);
                bytes += n;
            }
            in.close();
            return Long.toHexString(adler.getValue()) + "." + Long.toHexString(crc.getValue()) + "." + bytes;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Error();
        }
    }
