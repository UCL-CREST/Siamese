    private int binarySearch() {
        int low = 0;
        int high = size();
        int mid = 0;
        while (low < high) {
            mid = (low + high) / 2;
            if (greaterThan(mid)) {
                high = mid;
            } else {
                if (lessThan(mid)) {
                    low = mid + 1;
                } else {
                    return mid;
                }
            }
        }
        return -1;
    }
