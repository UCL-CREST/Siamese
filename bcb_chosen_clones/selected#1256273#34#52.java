    public int goExtended(int[] array, int target) {
        int start = 0;
        int end = array.length - 1;
        while (end > start) {
            int m = (start + end) / 2;
            if (array[m] == target) {
                end = m;
            } else if (array[m] > target) {
                end = m - 1;
            } else {
                start = m + 1;
            }
        }
        int p = end;
        if (p >= array.length || array[p] != target) {
            p = -1;
        }
        return p;
    }
