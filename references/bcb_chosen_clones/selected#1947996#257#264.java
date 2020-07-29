    private static int binarySearch(int[] a, int start, int len, int key) {
        int high = start + len, low = start - 1, guess;
        while (high - low > 1) {
            guess = (high + low) / 2;
            if (a[guess] < key) low = guess; else high = guess;
        }
        if (high == start + len) return ~(start + len); else if (a[high] == key) return high; else return ~high;
    }
