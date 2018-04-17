    public static int binarySearch(int val, int[] table) {
        if (table != null) {
            int len = table.length;
            int start = 0, end = len - 1, mid = (start + end) / 2;
            while (start <= end) {
                if (table[mid] == val) {
                    return mid;
                } else if (table[mid] < val) {
                    start = mid + 1;
                } else {
                    end = mid - 1;
                }
                mid = (start + end) / 2;
            }
        }
        return -1;
    }
