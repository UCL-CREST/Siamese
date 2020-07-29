    private final int search(char c, boolean exact) {
        int low = 0;
        int high = children.size() - 1;
        while (low <= high) {
            int middle = (low + high) / 2;
            char cmiddle = get(middle).getLabelStart();
            if (cmiddle < c) low = middle + 1; else if (c < cmiddle) high = middle - 1; else return middle;
        }
        if (exact) return -1;
        return high;
    }
