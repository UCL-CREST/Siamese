    private static <T extends Comparable<? super T>> int binarySearch(List<T> list, T element, int start, int end) {
        if (end < start) return -1;
        int mid = (start + end) / 2;
        T midObj = list.get(mid);
        int compare = midObj.compareTo(element);
        if (compare == 0) return mid;
        if (start == end) return -1;
        if (compare > 0) return binarySearch(list, element, start, mid - 1); else return binarySearch(list, element, mid + 1, end);
    }
