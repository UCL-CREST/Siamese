    public PrimeFactor[] primeFactorization(long x) {
        if (x <= 0) {
            throw new OnlyPositivePrimeFactorizationException(x);
        }
        List<PrimeFactor> res = new ArrayList<PrimeFactor>();
        for (long i = 2; i * i <= x; i++) {
            long n = 0;
            while (x % i == 0) {
                n++;
                x /= i;
            }
            if (n > 0) {
                res.add(new PrimeFactor(i, n));
            }
        }
        if (x > 1) {
            res.add(new PrimeFactor(x, 1));
        }
        return res.toArray(new PrimeFactor[0]);
    }
