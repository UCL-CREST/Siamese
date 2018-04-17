    public static boolean contains(int[] a, int value) {
        int lowerBound = 0;
        int upperBound = a.length - 1;
        while (upperBound - lowerBound >= 0) {
            int i = lowerBound + (upperBound - lowerBound) / 2;
            int v = a[i];
            if (v > value) {
                upperBound = i - 1;
            } else if (v < value) {
                lowerBound = i + 1;
            } else {
                return true;
            }
        }
        return false;
    }
