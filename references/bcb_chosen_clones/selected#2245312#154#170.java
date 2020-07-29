    int getUnitTypeIndex(String name) {
        int start, end, mid, c;
        start = 0;
        end = unitTypes.length;
        while (start < end) {
            mid = (start + end) / 2;
            c = unitTypes[mid].getName().compareTo(name);
            if (c == 0) {
                return mid;
            } else if (c > 0) {
                end = mid;
            } else {
                start = mid + 1;
            }
        }
        return -1;
    }
