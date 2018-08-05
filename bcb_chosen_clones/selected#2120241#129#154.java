    private int search(long time, List<Interval> intervals) {
        boolean success = false;
        int first = 0;
        int last = intervals.size();
        int middle = 0;
        int index = 0;
        if (time >= Long.MAX_VALUE) {
            time = Long.MAX_VALUE - 1;
        }
        while (!success && first <= last) {
            middle = (first + last) / 2;
            Interval midd = (Interval) intervals.get(middle);
            if (midd.contains(time)) {
                index = middle;
                success = true;
                break;
            }
            if (midd.isLess(time)) {
                last = middle - 1;
            }
            if (midd.isGreater(time)) {
                first = middle + 1;
            }
        }
        return index;
    }
