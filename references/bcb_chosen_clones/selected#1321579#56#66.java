    private int findIndexByAddress(long key) {
        int lo = 0;
        int hi = itemsByAddress.size() - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (key < ((HeapItem) itemsByAddress.get(mid)).address) hi = mid - 1; else if (key > ((HeapItem) itemsByAddress.get(mid)).address) lo = mid + 1; else return mid;
        }
        HeapItem item = getLastItem();
        if (item == null || key > item.address) return -1;
        return lo;
    }
