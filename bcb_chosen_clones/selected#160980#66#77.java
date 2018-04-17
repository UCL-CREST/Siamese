    private int indexOfValue(int val) {
        if (values.size() == 0) return -1;
        int lowIdx = 0;
        int highIdx = values.size() - 1;
        while (lowIdx <= highIdx) {
            int idx = (lowIdx + highIdx) / 2;
            int v = values.get(idx);
            if (v == val) return idx;
            if (val < v) highIdx = idx - 1; else lowIdx = idx + 1;
        }
        return -(lowIdx + 1);
    }
