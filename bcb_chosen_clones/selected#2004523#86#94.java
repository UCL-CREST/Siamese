    private int bsearch(int bnum) {
        int l = 0, r = size;
        while (l < r) {
            int p = (l + r) / 2;
            if (bnum < offs[p]) r = p; else if (bnum > offs[p]) l = p + 1; else return p;
        }
        CUtility.ASSERT(l == r);
        return l;
    }
