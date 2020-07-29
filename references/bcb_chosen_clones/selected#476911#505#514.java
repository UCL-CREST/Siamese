    private int getIndex(int fromIndex, char c) {
        if (fromIndex + 1 >= mFirstChild.length) return -1;
        int low = mFirstChild[fromIndex];
        int high = mFirstChild[fromIndex + 1] - 1;
        while (low <= high) {
            int mid = (high + low) / 2;
            if (mChars[mid] == c) return mid; else if (mChars[mid] < c) low = (low == mid) ? mid + 1 : mid; else high = (high == mid) ? mid - 1 : mid;
        }
        return -1;
    }
