    public static int binarySearch(Date[] days, Date d) {
        if (days.length == 0) {
            return -1;
        }
        int startPos = 0;
        int endPos = days.length - 1;
        int m = (startPos + endPos) / 2;
        while (startPos <= endPos) {
            if (d.compare(days[m]) == 0) {
                return m;
            }
            if (d.compare(days[m]) > 0) {
                startPos = m + 1;
            }
            if (d.compare(days[m]) < 0) {
                endPos = m - 1;
            }
            m = (startPos + endPos) / 2;
        }
        return -1;
    }
