    private int binarySearch(long key) {
        int low = 0;
        int high = (rangeCount * 2) - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            long midVal = ranges[mid];
            if (midVal < key) {
                low = mid + 1;
            } else if (midVal > key) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return -(low + 1);
    }
