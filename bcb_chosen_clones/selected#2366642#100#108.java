    public static void shuffle(double[] source, Random randomizer) {
        int n = source.length;
        for (int i = n - 1; i > 0; i--) {
            int j = randomizer.nextInt(i + 1);
            double k = source[j];
            source[j] = source[i];
            source[i] = k;
        }
    }
