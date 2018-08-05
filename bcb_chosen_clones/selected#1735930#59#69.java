    private static final int search(NodeProxy[] items, int low, int high, NodeProxy cmpItem) {
        int mid;
        int cmp;
        while (low <= high) {
            mid = (low + high) / 2;
            cmp = items[mid].compareTo(cmpItem);
            if (cmp == 0) return mid;
            if (cmp > 0) high = mid - 1; else low = mid + 1;
        }
        return -1;
    }
