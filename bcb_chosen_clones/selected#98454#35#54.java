    private int getRangeIndexOf(long v) {
        final int size = _ranges.size();
        if (size == 0) {
            return -1;
        }
        int lo = 0;
        int hi = size - 1;
        while (lo <= hi) {
            int mid = (lo + hi) / 2;
            LongRange r = _ranges.get(mid);
            if (r.contains(v)) {
                return mid;
            } else if (v < r.getStart()) {
                hi = mid - 1;
            } else {
                lo = mid + 1;
            }
        }
        return -(lo + 1);
    }
