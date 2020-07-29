    long getStartingSummary(long tick) {
        long low = 0, mid = 0, high = summaryStream.getLength() - 1;
        IEvent f = null;
        while (high >= low) {
            mid = (high + low) / 2;
            f = summaryStream.getEvent(mid);
            if (tick == f.getTime()) return mid; else if (tick < f.getTime()) high = mid - 1; else low = mid + 1;
        }
        return high >= 0 ? high : 0;
    }
