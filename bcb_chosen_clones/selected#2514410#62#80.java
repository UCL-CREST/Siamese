    public static long gcd(long a, long b) {
        long x;
        long y;
        if (a < 0) a = -a;
        if (b < 0) b = -b;
        if (a >= b) {
            x = a;
            y = b;
        } else {
            x = b;
            y = a;
        }
        while (y != 0) {
            long t = x % y;
            x = y;
            y = t;
        }
        return x;
    }
