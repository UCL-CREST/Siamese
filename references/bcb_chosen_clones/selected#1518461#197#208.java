    protected int findIndexOfTupleGreaterThan(double val) {
        int high = this.numTuples, low = -1, probe;
        while (high - low > 1) {
            probe = (high + low) / 2;
            if (this.summary[probe].v > val) {
                high = probe;
            } else {
                low = probe;
            }
        }
        return high;
    }
