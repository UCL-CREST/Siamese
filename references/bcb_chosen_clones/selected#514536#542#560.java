    public static int binarySearch(List list, Object key) {
        if (list instanceof AbstractSequentialList) {
            ListIterator i = list.listIterator();
            while (i.hasNext()) {
                int cmp = ((Comparable) (i.next())).compareTo(key);
                if (cmp == 0) return i.previousIndex(); else if (cmp > 0) return -i.nextIndex();
            }
            return -i.nextIndex() - 1;
        }
        int low = 0;
        int high = list.size() - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            Object midVal = list.get(mid);
            int cmp = ((Comparable) midVal).compareTo(key);
            if (cmp < 0) low = mid + 1; else if (cmp > 0) high = mid - 1; else return mid;
        }
        return -(low + 1);
    }
