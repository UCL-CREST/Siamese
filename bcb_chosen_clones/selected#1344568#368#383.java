    public static int binarySearch(List pl, Comparator c, Object p) {
        int l = 0;
        int u = pl.size() - 1;
        while (l <= u) {
            int m = (l + u) / 2;
            int v = c.compare(pl.get(m), p);
            if (v > 0) {
                l = m + 1;
            } else if (v < 0) {
                u = m - 1;
            } else {
                return m;
            }
        }
        return -(l + 1);
    }
