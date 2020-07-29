    public static int find(int[] a, int find) {
        int left = 0;
        int right = a.length - 1;
        int mid = (left + right) / 2;
        while (left <= right) {
            if (find < a[mid]) {
                right = mid - 1;
            } else if (find > a[mid]) {
                left = mid + 1;
            } else {
                return mid;
            }
            mid = (left + right) / 2;
        }
        return -1;
    }
