    protected synchronized int findIndex(int virtualIndex) {
        int low = 0;
        int high = realSize - 1;
        int mid;
        while (low <= high) {
            mid = (low + high) / 2;
            if (indices[mid] > virtualIndex) {
                high = mid - 1;
            } else if (indices[mid] < virtualIndex) {
                low = mid + 1;
            } else {
                return mid;
            }
        }
        throw new AssertionError("Addressing error! (could not find proper index)");
    }
