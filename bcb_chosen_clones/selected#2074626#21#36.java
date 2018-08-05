    public static int binarySearch(short[] list, int toFind) {
        int low = 0;
        int high = list.length - 1;
        int mid;
        while (low <= high) {
            mid = (low + high) / 2;
            if (list[mid] < toFind) {
                low = mid + 1;
            } else if (list[mid] > toFind) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return NOT_FOUND;
    }
