    public int binarySearch(Date dateTime) {
        long msDate = dateTime.getTime();
        int lo = 0, hi = this.size();
        while (lo < hi) {
            int mid = (lo + hi) / 2;
            long midMsDate = this.get(mid).getDate().getTime();
            if (msDate < midMsDate) hi = mid; else if (msDate > midMsDate) lo = mid + 1; else return mid;
        }
        return ~lo;
    }
