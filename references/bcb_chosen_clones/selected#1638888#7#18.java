    public PrimeFactors(int entier) {
        int n = entier;
        for (int i = 2; i <= n / i; i++) {
            while (n % i == 0) {
                add(i);
                n /= i;
            }
        }
        if (n > 1) {
            add(n);
        }
    }
