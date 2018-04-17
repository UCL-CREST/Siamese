    public static int binarySearch(List list, Object key, Comparator c) {
        if (c == null) return binarySearch(list, key);
        if (list instanceof AbstractSequentialList) {
            ListIterator i = list.listIterator();
            while (i.hasNext()) {
                int cmp = c.compare(i.next(), key);
                if (cmp == 0) return i.previousIndex(); else if (cmp > 0) return -i.nextIndex();
            }
            return -i.nextIndex() - 1;
        }
        int low = 0;
        int high = list.size() - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            Object midVal = list.get(mid);
            int cmp = c.compare(midVal, key);
            if (cmp < 0) low = mid + 1; else if (cmp > 0) high = mid - 1; else return mid;
        }
        return -(low + 1);
    }
