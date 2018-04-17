    public static final double fibonacciNumber(int n) {
        if (n < 0) return Double.NaN;
        if (n == 0) return 0;
        if (n == 1) return 1;
        return fibonacciNumber(n - 1) + fibonacciNumber(n - 2);
    }
