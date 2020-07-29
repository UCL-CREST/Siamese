    protected Integer insert(Integer j, Integer k) {
        if (isNonzero(k) && isGreaterThan(k, j) && isLessThan(new Integer(k.intValue() - 1), j)) {
            thresh.put(k, j);
        } else {
            int hi = -1;
            if (isNonzero(k)) {
                hi = k.intValue();
            } else if (thresh.size() > 0) {
                hi = ((Integer) thresh.lastKey()).intValue();
            }
            if (hi == -1 || j.compareTo(getLastValue()) > 0) {
                append(j);
                k = new Integer(hi + 1);
            } else {
                int lo = 0;
                while (lo <= hi) {
                    int index = (hi + lo) / 2;
                    Integer val = (Integer) thresh.get(new Integer(index));
                    int cmp = j.compareTo(val);
                    if (cmp == 0) {
                        return null;
                    } else if (cmp > 0) {
                        lo = index + 1;
                    } else {
                        hi = index - 1;
                    }
                }
                thresh.put(new Integer(lo), j);
                k = new Integer(lo);
            }
        }
        return k;
    }
