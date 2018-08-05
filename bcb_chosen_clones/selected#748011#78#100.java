    private Vector _sort(Pair[] ps, String id, int num) {
        Vector ret = new Vector();
        boolean swapped = true;
        int j = 0;
        Pair tmp;
        while (swapped) {
            swapped = false;
            j++;
            for (int i = 0; i < ps.length - j; i++) {
                if (ps[i].c > ps[i + 1].c) {
                    tmp = ps[i];
                    ps[i] = ps[i + 1];
                    ps[i + 1] = tmp;
                    swapped = true;
                }
            }
        }
        int m = Math.min(num, ps.length);
        for (int i = m - 1; i >= 0; i--) {
            if (id == null) ret.addElement(ps[i].n); else if (ps[i].n.startsWith(id) && !ps[i].n.equals(id)) ret.addElement(ps[i].n);
        }
        return ret;
    }
