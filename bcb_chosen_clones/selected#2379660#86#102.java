    public static final int binarySearch(Object[] a, Object key, int length) {
        int x1 = 0;
        int x2 = length;
        int i = x2 / 2, c;
        while (x1 < x2) {
            c = ((Comparable) a[i]).compareTo(key);
            if (c == 0) {
                return i;
            } else if (c < 0) {
                x1 = i + 1;
            } else {
                x2 = i;
            }
            i = x1 + (x2 - x1) / 2;
        }
        return -1 * (i + 1);
    }
