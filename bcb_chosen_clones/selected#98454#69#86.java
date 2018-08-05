    public int insertRange(LongRange other) {
        int lo = 0;
        int hi = _ranges.size() - 1;
        while (lo <= hi) {
            int mid = (lo + hi) / 2;
            LongRange r = _ranges.get(mid);
            final int compare = other.compareTo(r);
            if (compare == 0) {
                return -1;
            } else if (compare < 0) {
                hi = mid - 1;
            } else {
                lo = mid + 1;
            }
        }
        _ranges.add(lo, other);
        return lo;
    }
