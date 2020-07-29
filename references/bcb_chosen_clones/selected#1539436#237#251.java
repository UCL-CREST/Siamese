    private int insertionPosition(Item[] items, ViewerSorter sorter, int lastInsertion, Object element) {
        int size = items.length;
        if (sorter == null) return size;
        int min = lastInsertion, max = size - 1;
        while (min <= max) {
            int mid = (min + max) / 2;
            Object data = items[mid].getData();
            int compare = sorter.compare(this, data, element);
            if (compare == 0) {
                return mid;
            }
            if (compare < 0) min = mid + 1; else max = mid - 1;
        }
        return min;
    }
