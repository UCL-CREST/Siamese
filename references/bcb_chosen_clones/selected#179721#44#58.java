    private int search(int item) {
        int start = 0;
        int end = currentEnd;
        while (end > start) {
            int m = (start + end) / 2;
            if (item == values[m]) {
                return m;
            } else if (item > values[m]) {
                start = m + 1;
            } else {
                end = m - 1;
            }
        }
        return start;
    }
