    public static int binarySearch(Object[] list, Object toFind, Comparator comp) {
        int low = 0;
        int high = list.length - 1;
        int mid;
        while (low <= high) {
            mid = (low + high) / 2;
            if (comp.compare(list[mid], toFind) < 0) {
                low = mid + 1;
            } else if (comp.compare(list[mid], toFind) > 0) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        lowGlobal = low;
        return NOT_FOUND;
    }
