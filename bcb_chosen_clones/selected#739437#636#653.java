    public int getYearStatIndex(int year) {
        int start = 0;
        int end = yearStatsIdx - 1;
        int mid = end / 2;
        int found = -1;
        int n = -1;
        while (start <= end && found == -1) {
            if ((n = yearStats[mid].getYear()) == year) {
                found = mid;
            } else if (year < n) {
                end = mid - 1;
            } else {
                start = mid + 1;
            }
            mid = (start + end) / 2;
        }
        return found;
    }
