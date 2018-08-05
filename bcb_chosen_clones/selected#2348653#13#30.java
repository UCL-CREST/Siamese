    @Override
    public int idxOf(Comparable o) {
        int low = 0;
        int high = size() - 1;
        int mid, comparison;
        while (low <= high) {
            mid = (low + high) / 2;
            comparison = convert(get(mid)).compareTo(convert(o));
            if (comparison > 0) {
                high = mid - 1;
            } else if (comparison < 0) {
                low = mid + 1;
            } else {
                return mid;
            }
        }
        return -1;
    }
