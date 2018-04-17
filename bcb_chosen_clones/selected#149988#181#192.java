    private static long factorize(final List<PrimeFactor> primeFactors, final long value, final long factor) {
        int exponent = 0;
        long remainder = value;
        while (remainder % factor == 0) {
            ++exponent;
            remainder /= factor;
        }
        if (exponent != 0) {
            primeFactors.add(new PrimeFactor(BigInteger.valueOf(factor), exponent));
        }
        return remainder;
    }
