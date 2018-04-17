    protected int search(List<Long> v, long t) {
        int low = 0;
        int mid = -1;
        int high = v.size() - 1;
        while (low <= high) {
            mid = (low + high) / 2;
            long c = v.get(mid) - t;
            if (c > 0) high = mid - 1; else if (c < 0) low = mid + 1; else break;
        }
        return mid;
    }
