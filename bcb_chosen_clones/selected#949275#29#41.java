        private Factorisation(int entier) {
            factL = new ArrayList<Integer>();
            int n = entier;
            for (int i = 2; i <= n / i; i++) {
                while (n % i == 0) {
                    factL.add(i);
                    n /= i;
                }
            }
            if (n > 1) {
                factL.add(n);
            }
        }
