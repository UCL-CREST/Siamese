    public static int binarySearch(int vl, int[] vls, int from, int to) {
        int low = from;
        int high = to - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            if (vls[mid] < vl) {
                low = mid + 1;
            } else if (vls[mid] > vl) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return -1;
    }
