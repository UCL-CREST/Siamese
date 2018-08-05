    private int binarySearch(Object key, int low, int high) throws DException {
        int position = -1;
        SuperComparator comparator = btree.getComparator();
        while (low <= high) {
            int mid = (low + high) / 2;
            int cmp;
            cmp = comparator.compare(getKey(mid), key);
            if (cmp < 0) low = mid + 1; else if (cmp > 0) high = mid - 1; else {
                position = mid;
                if (!btree.getDuplicateAllowed()) break;
                low = mid + 1;
            }
        }
        return position == -1 ? -(low - 1) : position;
    }
