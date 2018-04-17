    public static long compute(String str) {
        CRC32 crc = new CRC32();
        Adler32 adler = new Adler32();
        crc.update(str.getBytes());
        adler.update(str.getBytes());
        long dg1 = crc.getValue();
        long dg2 = adler.getValue();
        crc.update(String.valueOf(dg2).getBytes());
        adler.update(String.valueOf(dg1).getBytes());
        long d3 = crc.getValue();
        long d4 = adler.getValue();
        dg1 ^= d4;
        dg2 ^= d3;
        return (dg2 ^ ((dg1 >>> 32) | (dg1 << 32)));
    }
