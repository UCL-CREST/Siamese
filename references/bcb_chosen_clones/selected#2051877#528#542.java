    private int search(BTreeNode cluster, Object keyToSeek, int low, int high, boolean flag) throws DException {
        int cmp = 0;
        int position = -1;
        Object object = null;
        while (low <= high) {
            int mid = (low + high) / 2;
            object = cluster.getKey(mid);
            cmp = comparator.compare(keyToSeek, object);
            if (cmp > 0) low = mid + 1; else if (cmp < 0) high = mid - 1; else {
                if (cmp == 0) position = mid;
                if (flag) high = mid - 1; else low = mid + 1;
            }
        }
        return position == -1 ? -(low - 1) : position;
    }
