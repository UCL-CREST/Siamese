    public static int turns(int n, int low, int high) {
        int turns = 0;
        while (high - low >= 2) {
            turns++;
            int mid = (low + high) / 2;
            if (mid == n) {
                return turns;
            } else if (mid < n) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        assert (n == low || n == high);
        return 1 + turns;
    }
