    public static int peakBinarySearch(double lowMass, double highMass, ArrayList peakList) {
        int low = 0;
        int high = peakList.size() - 1;
        int middle = -1;
        Peak tmpPeak = null;
        while (low <= high) {
            middle = (low + high) / 2;
            tmpPeak = (Peak) peakList.get(middle);
            if (lowMass > tmpPeak.mass) low = middle + 1; else if (highMass < tmpPeak.mass) high = middle - 1; else if (tmpPeak.mass >= lowMass && tmpPeak.mass <= highMass) {
                return middle;
            }
        }
        return -1;
    }
