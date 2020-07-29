    private int binarySearch(Object key) {
        String keyString = key.toString();
        int low = 0;
        int high = this.size() - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            String midVal = get(mid).toString();
            int cmp = midVal.compareToIgnoreCase(keyString);
            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return -(low + 1);
    }
