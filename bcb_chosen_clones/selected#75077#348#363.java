    private int binarySearch(Object key) {
        int lower = fromIndex;
        int upper = toIndex - 1;
        while (lower <= upper) {
            int middle = lower + (upper - lower) / 2;
            int c = ImmutableSortedSet.unsafeCompare(comparator, key, entries[middle].getKey());
            if (c < 0) {
                upper = middle - 1;
            } else if (c > 0) {
                lower = middle + 1;
            } else {
                return middle;
            }
        }
        return -lower - 1;
    }
