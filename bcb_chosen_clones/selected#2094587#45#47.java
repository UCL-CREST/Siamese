    public static int greatestCommonDivisor(int a, int b) {
        if (b == 0) return a; else return greatestCommonDivisor(b, a % b);
    }
