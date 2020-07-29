    public final int binarySearch(final int element) {
        int l, h;
        for (l = 0, h = size - 1; l <= h; ) {
            int middle = (h + l) / 2;
            int v = data[middle];
            if (v == element) return middle; else if (v < element) l = middle + 1; else if (v > element) h = middle - 1;
        }
        return -1;
    }
