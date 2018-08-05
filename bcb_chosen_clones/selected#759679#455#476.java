    private static int YearFromTime(double t) {
        int lo = (int) Math.floor((t / msPerDay) / 366) + 1970;
        int hi = (int) Math.floor((t / msPerDay) / 365) + 1970;
        int mid;
        if (hi < lo) {
            int temp = lo;
            lo = hi;
            hi = temp;
        }
        while (hi > lo) {
            mid = (hi + lo) / 2;
            if (TimeFromYear(mid) > t) {
                hi = mid - 1;
            } else {
                lo = mid + 1;
                if (TimeFromYear(lo) > t) {
                    return mid;
                }
            }
        }
        return lo;
    }
