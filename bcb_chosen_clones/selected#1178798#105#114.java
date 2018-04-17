    private final int search(int x) {
        int low = 0;
        int high = list.size() - 1;
        while (low <= high) {
            int i = (low + high) / 2;
            int li = get(i).low;
            if (li < x) low = i + 1; else if (x < li) high = i - 1; else return i;
        }
        return high;
    }
