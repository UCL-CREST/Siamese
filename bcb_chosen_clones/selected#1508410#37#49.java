    public static int binarySearch(int[] a, int find, int start, int end) {
        if (start > end) {
            return -1;
        }
        int mid = (start + end) / 2;
        if (find > a[mid]) {
            return binarySearch(a, find, mid + 1, end);
        } else if (find < a[mid]) {
            return binarySearch(a, find, start, mid - 1);
        } else {
            return mid;
        }
    }
