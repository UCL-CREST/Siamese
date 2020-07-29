    private int findAt(E e) {
        int lower = 0, upper = list.size() - 1;
        int mid, comparison;
        while (lower <= upper) {
            mid = (lower + upper) / 2;
            comparison = comp.compare(e, list.get(mid));
            comparisons++;
            if (comparison < 0) {
                upper = mid - 1;
            } else if (comparison > 0) {
                lower = mid + 1;
            } else {
                return mid;
            }
        }
        return -1;
    }
