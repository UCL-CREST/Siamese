    void factorOut(long i, List<Long> factors1) {
        while (x % i == 0) {
            factors1.add(i);
            x = x / i;
        }
    }
