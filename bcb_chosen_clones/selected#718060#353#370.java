    private static boolean _contains(PowerDenotator p, Denotator d) {
        ArrayList<Denotator> factors = p.getListMorphismMap().getFactors();
        int i = 0;
        int j = factors.size() - 1;
        while (i <= j) {
            int t = (i + j) / 2;
            Denotator s = factors.get(t);
            int c = d.compareTo(s);
            if (c == 0) {
                return true;
            } else if (c < 0) {
                j = t - 1;
            } else {
                i = t + 1;
            }
        }
        return false;
    }
