    public static final int binarySearch(int[] a, int key, int begin, int end) {
        int x1 = begin;
        int x2 = end;
        int i = x1 + (x2 - x1) / 2;
        while (x1 < x2) {
            if (a[i] == key) {
                return i;
            } else if (a[i] < key) {
                x1 = i + 1;
            } else {
                x2 = i;
            }
            i = x1 + (x2 - x1) / 2;
        }
        return -1 * (i + 1);
    }
