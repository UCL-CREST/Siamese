    protected final int search(int key) {
        int beg = 0;
        int end = size();
        while (beg < end) {
            int med = (beg + end) / 2;
            Pair p = at(med);
            if (key < p.key) end = med; else if (key > p.key) beg = med + 1; else return med;
        }
        return beg;
    }
