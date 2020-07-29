    private int rbsearch(int l, int h, int tot, int costs) {
        if (l > h) {
            return l;
        }
        int cur = (l + h) / 2;
        int ot = ((Node) this.nodes.elementAt(cur)).f;
        if ((tot < ot) || (tot == ot && costs >= ((Node) this.nodes.elementAt(cur)).g)) {
            return rbsearch(l, cur - 1, tot, costs);
        }
        return rbsearch(cur + 1, h, tot, costs);
    }
