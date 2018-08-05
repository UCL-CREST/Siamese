    private final int findObject(SnmpOid oid) {
        int low = 0;
        int max = size - 1;
        SnmpOid pos;
        int comp;
        int curr = low + (max - low) / 2;
        while (low <= max) {
            pos = tableoids[curr];
            comp = oid.compareTo(pos);
            if (comp == 0) return curr;
            if (oid.equals(pos) == true) {
                return curr;
            }
            if (comp > 0) {
                low = curr + 1;
            } else {
                max = curr - 1;
            }
            curr = low + (max - low) / 2;
        }
        return -1;
    }
