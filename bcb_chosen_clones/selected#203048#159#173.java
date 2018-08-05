    public static int SNPbinarySearch(SNPDB[] a, int x) {
        int low = 0;
        int high = a.length - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            if (a[mid].start < x) {
                low = mid + 1;
            } else if (a[mid].start > x) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return -1;
    }
