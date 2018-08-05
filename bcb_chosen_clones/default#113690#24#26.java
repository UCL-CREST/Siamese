    static long seqFib(int n) {
        if (n <= 1) return n; else return seqFib(n - 1) + seqFib(n - 2);
    }
