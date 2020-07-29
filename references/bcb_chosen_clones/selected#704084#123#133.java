    int binarySearch(Comparable[] a, int low, int high, Comparable key) {
        int cmp;
        while (low <= high) {
            int mid = (low + high) / 2;
            Comparable midVal = a[mid];
            cmp = midVal.compareTo(key);
            if (entry.descending) cmp = -cmp;
            if (cmp < 0) low = mid + 1; else if (cmp > 0) high = mid - 1; else return mid;
        }
        return -(low + 1);
    }
