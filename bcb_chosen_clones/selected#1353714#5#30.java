    public static byte[] CRCe(byte[] src) {
        java.util.zip.CRC32 x = new java.util.zip.CRC32();
        x.update(src);
        long temp = x.getValue();
        byte[] crcb = Convertion.longToUInt(temp);
        int len = src.length;
        int mod = len % 4;
        int rem = 4 - mod;
        int dstSize = len;
        if (mod != 0) dstSize += rem;
        if (dstSize < 4) dstSize = 4;
        byte[] dst = new byte[dstSize];
        int i = 0;
        int j = 0;
        for (j = 0; j < len; ++j) {
            dst[j] = (byte) (src[j] ^ crcb[i % 4]);
            ++i;
        }
        int p = 4;
        if (len != 0) {
            p = 0;
            if (mod != 0) p = rem;
        }
        for (int k = 0; k < p; ++k) dst[j++] = (byte) (p ^ crcb[k]);
        return dst;
    }
