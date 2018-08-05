    private final int getInsertionPoint(SnmpOid oid, boolean fail) throws SnmpStatusException {
        final int failStatus = SnmpStatusException.snmpRspNotWritable;
        int low = 0;
        int max = size - 1;
        SnmpOid pos;
        int comp;
        int curr = low + (max - low) / 2;
        while (low <= max) {
            pos = tableoids[curr];
            comp = oid.compareTo(pos);
            if (comp == 0) {
                if (fail) throw new SnmpStatusException(failStatus, curr); else return curr + 1;
            }
            if (comp > 0) {
                low = curr + 1;
            } else {
                max = curr - 1;
            }
            curr = low + (max - low) / 2;
        }
        return curr;
    }
