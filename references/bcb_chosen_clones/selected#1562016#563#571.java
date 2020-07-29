    protected static int binarySearchFromTo(int[] array, int from, int to, IntComparator comp) {
        final int key = 0;
        while (from <= to) {
            int mid = (from + to) / 2;
            int comparison = comp.compare(array[mid], array[key]);
            if (comparison < 0) from = mid + 1; else if (comparison > 0) to = mid - 1; else return mid;
        }
        return -(from + 1);
    }
