    int binarySearch(int[] a, int x) {
        int result = -1;
        int mid = 0;
        int left = 0;
        int right = a.length - 1;
        while (result == -1 && left <= right) {
            mid = (left + right) / 2;
            if (a[mid] == x) {
                result = mid;
            } else {
                if (a[mid] > x) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
        }
        return result;
    }
