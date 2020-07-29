    public int find(double value) {
        int left = 0;
        int right = size() - 1;
        int mid;
        double mval;
        while (left < right) {
            mid = (left + right) / 2;
            mval = contents[mid];
            if (value == mval) return mid; else if (value < contents[mid]) right = mid - 1; else left = mid + 1;
        }
        return left;
    }
