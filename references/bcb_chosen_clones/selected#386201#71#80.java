    private static int binarySearch(Comparable[] a, Comparable x, int highestValidIndex) {
        int low = 0;
        int high = highestValidIndex;
        int mid;
        while (low <= high) {
            mid = (low + high) / 2;
            if (a[mid].compareTo(x) < 0) low = mid + 1; else if (a[mid].compareTo(x) > 0) high = mid - 1; else return mid;
        }
        return low;
    }
