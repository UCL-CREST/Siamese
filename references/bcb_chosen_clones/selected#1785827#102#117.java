    public static CanonFlopDetail containing(int turnCanon) {
        int lo = 0;
        int hi = Flop.CANONS - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            CanonRange turnRange = DETAILS.getTurnRange(mid);
            if (turnRange.from() > turnCanon) {
                hi = mid - 1;
            } else if (turnRange.toInclusive() < turnCanon) {
                lo = mid + 1;
            } else {
                return lookup(mid);
            }
        }
        return null;
    }
