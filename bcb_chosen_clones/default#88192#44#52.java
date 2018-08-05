    int gdc(int n, int d) {
        int rem = n % d;
        while (rem != 0) {
            n = d;
            d = rem;
            rem = n % d;
        }
        return d;
    }
