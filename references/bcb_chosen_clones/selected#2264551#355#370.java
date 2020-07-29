    private int binarySlotSearch() {
        int low = 0;
        int high = count;
        int mid = 0;
        int compare = 0;
        while (low < high) {
            mid = (low + high) / 2;
            compare = compare(mid);
            if (compare <= 0) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        return low;
    }
