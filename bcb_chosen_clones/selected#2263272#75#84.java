    private static int binarySearch(int[] a, int count, int[] key, int size, Comparator c) {
        int low = 0;
        int high = count;
        while (low <= high) {
            int mid = (low + high) / 2;
            int cmp = c.compare(a, mid * size, key, 0);
            if (cmp < 0) low = mid + 1; else if (cmp > 0) high = mid - 1; else return mid;
        }
        return -(low + 1);
    }
