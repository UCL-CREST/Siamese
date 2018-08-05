    private static <T> int binarySearch(List<T> list, T element, int start, int end, Comparator<? super T> comparer) {
        if (end < start) return -1;
        int mid = (start + end) / 2;
        T midObj = list.get(mid);
        int compare = comparer.compare(midObj, element);
        if (compare == 0) return mid;
        if (start == end) return -1;
        if (compare > 0) return binarySearch(list, element, start, mid - 1, comparer); else return binarySearch(list, element, mid + 1, end, comparer);
    }
