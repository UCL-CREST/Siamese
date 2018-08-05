    protected int findGTE(int[] list, int start, int len, int value) {
        int low = start;
        int high = start + (len - 1);
        int end = high;
        while (low <= high) {
            int mid = (low + high) / 2;
            int c = list[mid];
            if (c > value) high = mid - 1; else if (c < value) low = mid + 1; else return mid;
        }
        return (low <= end && list[low] > value) ? low : -1;
    }
