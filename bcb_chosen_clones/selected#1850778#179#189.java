    public int binarySearchFromTo(Object key, int from, int to) {
        int low = from;
        int high = to;
        while (low <= high) {
            int mid = (low + high) / 2;
            Object midVal = elements[mid];
            int cmp = ((Comparable) midVal).compareTo(key);
            if (cmp < 0) low = mid + 1; else if (cmp > 0) high = mid - 1; else return mid;
        }
        return -(low + 1);
    }
