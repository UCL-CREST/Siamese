    protected int findInSortedSuballocatedIntVector(SuballocatedIntVector vector, int lookfor) {
        int i = 0;
        if (vector != null) {
            int first = 0;
            int last = vector.size() - 1;
            while (first <= last) {
                i = (first + last) / 2;
                int test = lookfor - vector.elementAt(i);
                if (test == 0) {
                    return i;
                } else if (test < 0) {
                    last = i - 1;
                } else {
                    first = i + 1;
                }
            }
            if (first > i) {
                i = first;
            }
        }
        return -1 - i;
    }
