    private int findIndexBySize(long key) {
        int lo = 0;
        int hi = itemsBySize.size() - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (key < ((HeapItem) itemsBySize.get(mid)).size) hi = mid - 1; else if (key > ((HeapItem) itemsBySize.get(mid)).size) lo = mid + 1; else return mid;
        }
        HeapItem item = getLargestItem();
        if (item == null || key > item.size) return -1;
        return lo;
    }
