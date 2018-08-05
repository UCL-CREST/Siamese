    public static long gcd(long n, long m) {
        if (n == 0 || m == 0) return 0;
        while (n != m) if (n > m) n = n - m; else m = m - n;
        return n;
    }
