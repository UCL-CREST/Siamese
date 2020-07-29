    private int bsearch(int l, int h, double tot, double costs, List nodes) {
        int lo = l;
        int hi = h;
        while (lo <= hi) {
            int cur = (lo + hi) / 2;
            double ot = ((NodeDouble) nodes.elementAt(cur)).f;
            if ((tot < ot) || (tot == ot && costs >= ((NodeDouble) nodes.elementAt(cur)).g)) {
                hi = cur - 1;
            } else {
                lo = cur + 1;
            }
        }
        return lo;
    }
