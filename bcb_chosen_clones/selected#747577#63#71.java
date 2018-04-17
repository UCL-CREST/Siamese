    private static <T extends Comparable<? super T>> int binaryFind(List<T> list, T element, int start, int end) {
        if (end < start) return start;
        int mid = (start + end) / 2;
        T midObj = list.get(mid);
        int compare = midObj.compareTo(element);
        if (start == end) return compare < 0 ? start + 1 : start;
        if (compare == 0) return mid;
        if (compare > 0) return binaryFind(list, element, start, mid - 1); else return binaryFind(list, element, mid + 1, end);
    }
