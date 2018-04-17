    private int rbsearch(int l, int h, double tot, double costs, List nodes) {
        if (l > h) {
            return l;
        }
        int cur = (l + h) / 2;
        double ot = ((NodeDouble) nodes.elementAt(cur)).f;
        if ((tot < ot) || (tot == ot && costs >= ((NodeDouble) nodes.elementAt(cur)).g)) {
            return rbsearch(l, cur - 1, tot, costs, nodes);
        }
        return rbsearch(cur + 1, h, tot, costs, nodes);
    }
