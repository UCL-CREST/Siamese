    private int bsearch(int l, int h, int tot, int costs) {
        int lo = l;
        int hi = h;
        while (lo <= hi) {
            int cur = (lo + hi) / 2;
            int ot = ((Node) this.nodes.elementAt(cur)).f;
            if ((tot < ot) || (tot == ot && costs >= ((Node) this.nodes.elementAt(cur)).g)) {
                hi = cur - 1;
            } else {
                lo = cur + 1;
            }
        }
        return lo;
    }
