    protected static final int getNextIdentifier(int table[], long value) throws SnmpStatusException {
        final int[] a = table;
        final int val = (int) value;
        if (a == null) throw noSuchObjectException;
        int low = 0;
        int max = a.length;
        int curr = low + (max - low) / 2;
        int elmt = 0;
        if (max < 1) throw noSuchObjectException;
        if (a[max - 1] <= val) throw noSuchObjectException;
        while (low <= max) {
            elmt = a[curr];
            if (val == elmt) {
                curr++;
                return a[curr];
            }
            if (elmt < val) {
                low = curr + 1;
            } else {
                max = curr - 1;
            }
            curr = low + (max - low) / 2;
        }
        return a[curr];
    }
