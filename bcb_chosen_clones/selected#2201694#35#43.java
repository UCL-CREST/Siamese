    public static int search(IList ls, Object k, IComparator c, int low, int high) {
        while (low <= high) {
            int mid = (low + high) / 2;
            Object midVal = ls.get(mid);
            int cmp = c.compare(midVal, k);
            if (cmp < 0) low = mid + 1; else if (cmp > 0) high = mid - 1; else return mid;
        }
        return -(low + 1);
    }
