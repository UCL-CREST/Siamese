    public static int binarySearch(int[] arrayOfInts, int intToBeFound) {
        int low = 0;
        int high = arrayOfInts.length - 1;
        int mid;
        while (low <= high) {
            mid = (low + high) / 2;
            if (arrayOfInts[mid] > intToBeFound) {
                low = mid + 1;
            } else if (arrayOfInts[mid] < intToBeFound) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return -1;
    }
