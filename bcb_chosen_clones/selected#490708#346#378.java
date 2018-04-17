    protected Integer insert(Integer j, Integer k) {
        if (isNonzero(k) && isGreaterThan(k, j) && isLessThan(k - 1, j)) {
            thresh.put(k, j);
        } else {
            int high = -1;
            if (isNonzero(k)) {
                high = k;
            } else if (thresh.size() > 0) {
                high = thresh.lastKey();
            }
            if (high == -1 || j.compareTo(getLastValue()) > 0) {
                append(j);
                k = high + 1;
            } else {
                int low = 0;
                while (low <= high) {
                    int index = (high + low) / 2;
                    Integer val = thresh.get(index);
                    int cmp = j.compareTo(val);
                    if (cmp == 0) {
                        return null;
                    } else if (cmp > 0) {
                        low = index + 1;
                    } else {
                        high = index - 1;
                    }
                }
                thresh.put(low, j);
                k = low;
            }
        }
        return k;
    }
