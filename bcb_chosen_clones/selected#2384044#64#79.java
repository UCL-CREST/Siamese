    private static int binarySearchArray(int f, int[] s) {
        int low = 0;
        int high = s.length - 1;
        int mid;
        while (low <= high) {
            mid = (low + high) / 2;
            if (s[mid] > f) {
                high = mid - 1;
            } else if (s[mid] < f) {
                low = mid + 1;
            } else {
                return mid;
            }
        }
        return -1;
    }
