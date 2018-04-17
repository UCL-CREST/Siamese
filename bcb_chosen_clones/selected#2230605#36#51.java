    private int binarySearch(Object left, Object[] rightArray) throws DException {
        int low = 0, high = rightArray.length - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            SuperComparator comparator = comparators[mid];
            int returnValue = comparator.compare(left, rightArray[mid]);
            if (returnValue < 0) {
                high = mid - 1;
            } else if (returnValue > 0) {
                low = mid + 1;
            } else {
                return 0;
            }
        }
        return -1;
    }
