    public int binarySearch(double value) {
        int start = 0;
        int end = instances.size() - 1;
        while (start <= end) {
            int v = (start + end) / 2;
            if ((Double) instances.elementAt(v) == value) {
                return v;
            } else if ((Double) instances.elementAt(v) < value) {
                start = v + 1;
            } else {
                end = v - 1;
            }
        }
        return -1;
    }
