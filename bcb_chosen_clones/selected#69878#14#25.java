    private void calculate(int entier) {
        double n = entier;
        for (double i = 2; i <= n / i; i++) {
            while (n % i == 0) {
                add(i);
                n /= i;
            }
        }
        if (n > 1) {
            add(n);
        }
    }
