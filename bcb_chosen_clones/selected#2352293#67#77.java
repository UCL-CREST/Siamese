    static int findLine(IntArrayList lineLocation, int location) {
        int lo = 0;
        int hi = lineLocation.size() - 1;
        if (hi == 0) return 0;
        do {
            int mid = lo + (hi - lo) / 2;
            if (lineLocation.get(mid) == location) return mid;
            if (lineLocation.get(mid) < location) lo = mid + 1; else hi = mid - 1;
        } while (lo < hi);
        return (lineLocation.get(lo) > location ? lo - 1 : lo);
    }
