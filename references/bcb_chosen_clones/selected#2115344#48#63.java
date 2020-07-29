    private final int indexOf(String stringI) {
        int lo = 0, hi = strings.size() - 1, idx;
        String entry;
        for (idx = (hi + lo) / 2; lo <= hi; idx = (hi + lo) / 2) {
            entry = (String) strings.elementAt(idx);
            int cmpr = entry.compareTo(stringI);
            if (cmpr == 0) {
                return (idx);
            } else if (cmpr < 0) {
                lo = idx + 1;
            } else {
                hi = idx - 1;
            }
        }
        return (-(lo + 1));
    }
