    private int searchPreIndex(final int lv, final int lb, final int rb, final int l, final int r) {
        int index = -1;
        int ll = l;
        int rr = r;
        while (rr >= ll && index == -1) {
            final int m = ll + (rr - ll) / 2;
            if (nodes[lv][m] < lb) {
                ll = m + 1;
            } else if (nodes[lv][m] > rb) {
                rr = m - 1;
            } else {
                index = m;
            }
        }
        return index;
    }
