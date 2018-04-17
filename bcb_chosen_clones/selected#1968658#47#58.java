    private int indexOfKey(int key) {
        if (keys.size() == 0) return -1;
        int lowIdx = 0;
        int highIdx = keys.size() - 1;
        while (lowIdx <= highIdx) {
            int idx = (lowIdx + highIdx) / 2;
            int k = keys.get(idx);
            if (k == key) return idx;
            if (key < k) highIdx = idx - 1; else lowIdx = idx + 1;
        }
        return -(lowIdx + 1);
    }
