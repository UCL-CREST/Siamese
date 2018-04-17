    public Factorization(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Can't factorize non-positive numbers");
        }
        for (int p : PRIMES) {
            if (p > n) {
                break;
            }
            int exp = 0;
            while (n % p == 0) {
                exp++;
                n /= p;
            }
            if (exp > 0) {
                factors.put(p, exp);
            }
        }
        if (n != 1) {
            factors.put(n, 1);
        }
    }
