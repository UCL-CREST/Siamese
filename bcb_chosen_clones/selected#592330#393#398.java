    public static final double lucasNumber(int n) {
        if (n < 0) return Double.NaN;
        if (n == 0) return 2;
        if (n == 1) return 1;
        return lucasNumber(n - 1) + lucasNumber(n - 2);
    }
