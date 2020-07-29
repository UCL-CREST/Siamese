    private static final NodeSet searchRange(ArraySet result, NodeProxy[] items, int low, int high, NodeProxy lower, NodeProxy upper) {
        int mid = 0;
        int max = high;
        int cmp;
        while (low <= high) {
            mid = (low + high) / 2;
            cmp = items[mid].compareTo(lower);
            if (cmp == 0) break;
            if (cmp > 0) high = mid - 1; else low = mid + 1;
        }
        while (mid > 0 && items[mid].compareTo(lower) > 0) mid--;
        if (items[mid].compareTo(lower) < 0) mid++;
        while (mid <= max && items[mid].compareTo(upper) <= 0) result.add(items[mid++]);
        result.setIsSorted(true);
        return result;
    }
