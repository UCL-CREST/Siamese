    private static int binarySearch(int[] index, double[] vals, double target) {
        int lo = 0, hi = index.length - 1;
        while (hi - lo > 1) {
            int mid = lo + (hi - lo) / 2;
            double midval = vals[index[mid]];
            if (target > midval) {
                lo = mid;
            } else if (target < midval) {
                hi = mid;
            } else {
                while ((mid > 0) && (vals[index[mid - 1]] == target)) {
                    mid--;
                }
                return mid;
            }
        }
        return lo;
    }
