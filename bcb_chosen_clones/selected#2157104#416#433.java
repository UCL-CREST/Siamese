    private int binarySearch() {
        int low = 0;
        int high = rowCount;
        int mid = 0;
        while (low < high) {
            mid = (low + high) / 2;
            if (rowComparator.greaterThan(mid)) {
                high = mid;
            } else {
                if (rowComparator.lessThan(mid)) {
                    low = mid + 1;
                } else {
                    return mid;
                }
            }
        }
        return -1;
    }
