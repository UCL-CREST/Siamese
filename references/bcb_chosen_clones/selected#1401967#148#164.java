    public int get(String key) {
        int low = 0;
        int high = size - 1;
        int cmp = 0;
        while (low <= high) {
            int mid = (low + high) / 2;
            cmp = keys[mid].compareTo(key);
            if (cmp > 0) {
                high = mid - 1;
            } else if (cmp < 0) {
                low = mid + 1;
            } else {
                return mid;
            }
        }
        return -1 - low;
    }
