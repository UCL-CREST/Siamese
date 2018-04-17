    protected int findIndex(int virtualIndex) {
        int size = indices.size();
        if (size == 0) {
            return -1;
        } else if (size == 1) {
            if (indices.get(0) == virtualIndex) {
                return 0;
            } else {
                return -1;
            }
        }
        int low = 0;
        int high = size - 1;
        int mid, current;
        while (low <= high) {
            mid = (low + high) / 2;
            current = indices.get(mid);
            if (current > virtualIndex) {
                high = mid - 1;
            } else if (current < virtualIndex) {
                low = mid + 1;
            } else {
                return -1;
            }
        }
        return -1;
    }
