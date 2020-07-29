    public int binarySearchFromTo(boolean key, int from, int to) {
        int low = from;
        int high = to;
        int intKey = toInt(key);
        while (low <= high) {
            int mid = (low + high) / 2;
            boolean midVal = get(mid);
            if (toInt(midVal) < intKey) low = mid + 1; else if (toInt(midVal) > intKey) high = mid - 1; else return mid;
        }
        return -(low + 1);
    }
