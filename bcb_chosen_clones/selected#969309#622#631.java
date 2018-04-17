    public int findNeighbor(float targetVal, float[] values) {
        int low = 0;
        int high = values.length - 1;
        int middle;
        while (low <= high) {
            middle = (low + high) / 2;
            if (targetVal == values[middle]) return middle; else if (targetVal < values[middle]) high = middle - 1; else low = middle + 1;
        }
        if (Math.abs(targetVal - values[high]) > Math.abs(targetVal - values[low])) return low; else return high;
    }
