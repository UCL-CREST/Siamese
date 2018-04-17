    private static int binarySearch(int[] a, int val) {
        int low = 0;
        int high = a.length - 1;
        int mid = 0;
        int numnComparisons = 0;
        while (low <= high) {
            mid = (low + high) / 2;
            if (a[mid] > val) {
                high = mid - 1;
                numnComparisons++;
            } else if (a[mid] < val) {
                low = mid + 1;
                numnComparisons++;
            } else return mid;
        }
        return -1;
    }
