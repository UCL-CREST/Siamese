    private int binarySearch(String keyword) {
        int beg = 0;
        for (int end = keys.length - 1; beg <= end; ) {
            int mid = (beg + end) / 2;
            int res = ignoreCaseComparator.compare(keys[mid], keyword);
            if (res < 0) beg = mid + 1; else if (res > 0) end = mid - 1; else return mid;
        }
        return -beg;
    }
