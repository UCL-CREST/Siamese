    public int GetLastBaseBeforeSample(int sampleNum) {
        int first, last, mid;
        first = 1;
        last = base.length - 1;
        mid = (last - first) / 2;
        while ((last - first) >= 2) {
            if (sampleNum < basePosition[mid]) {
                last = mid;
            } else if (sampleNum > basePosition[mid]) {
                first = mid;
            } else {
                return (mid);
            }
            mid = first + (last - first) / 2;
        }
        if ((sampleNum > basePosition[first]) && (sampleNum < basePosition[last])) {
            return (first);
        }
        if (sampleNum > basePosition[last]) {
            return (last);
        }
        return (0);
    }
