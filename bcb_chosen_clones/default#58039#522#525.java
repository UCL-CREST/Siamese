    public static int GCD(int a, int b) {
        if (b == 0) return a;
        return GCD(b, a % b);
    }
