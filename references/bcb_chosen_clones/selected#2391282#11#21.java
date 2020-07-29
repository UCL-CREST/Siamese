    public int lookup(long value) {
        int low = 0;
        int high = size() - 1;
        int mid;
        while (low <= high) {
            mid = (low + high) / 2;
            int cmpVal = cmp(mid, value);
            if (cmpVal < 0) low = mid + 1; else if (cmpVal > 0) high = mid - 1; else return mid;
        }
        return -1 - low;
    }
