    public static int binarySearch(String[] arr, String elem, int fromIndex, int toIndex) {
        int mid, cmp;
        while (fromIndex <= toIndex) {
            mid = (fromIndex + toIndex) / 2;
            if ((cmp = arr[mid].compareTo(elem)) < 0) {
                fromIndex = mid + 1;
            } else if (cmp > 0) {
                toIndex = mid - 1;
            } else {
                return mid;
            }
        }
        return fromIndex;
    }
