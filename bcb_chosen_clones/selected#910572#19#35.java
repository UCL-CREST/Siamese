    public static int binarySearch(Comparable[] list, Comparable toFind) {
        int low = 0;
        int high = list.length - 1;
        int mid;
        while (low <= high) {
            mid = (low + high) / 2;
            if (list[mid].compareTo(toFind) < 0) {
                low = mid + 1;
            } else if (list[mid].compareTo(toFind) > 0) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        lowGlobal = low;
        return NOT_FOUND;
    }
