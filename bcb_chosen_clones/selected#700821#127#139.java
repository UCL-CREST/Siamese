    public static List<Long> primeFactors(long n) {
        List<Long> factors = new ArrayList<Long>();
        for (int i = 2; i <= n / i; i++) {
            while (n % i == 0) {
                factors.add((long) i);
                n /= i;
            }
        }
        if (n > 1) {
            factors.add(n);
        }
        return factors;
    }
