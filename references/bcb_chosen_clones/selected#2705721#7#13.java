    public static int gcd(int m, int n) {
        if (0 == n) {
            return m;
        } else {
            return gcd(n, m % n);
        }
    }
