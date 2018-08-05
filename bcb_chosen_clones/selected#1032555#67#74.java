    private static int binarySearchFromTo(int a, int from, int to, IntComparator comp) {
        while (from <= to) {
            int mid = (from + to) / 2;
            int comparison = comp.compare(mid, a);
            if (comparison < 0) from = mid + 1; else if (comparison > 0) to = mid - 1; else return mid;
        }
        return -(from + 1);
    }
