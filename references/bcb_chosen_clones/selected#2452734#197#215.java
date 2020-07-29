    public int findIdx(int ofs) {
        int low, high;
        int idx;
        int tmp;
        low = 0;
        high = instructions.size() - 1;
        while (low <= high) {
            idx = (low + high) / 2;
            tmp = instructions.get(idx).ofs;
            if (tmp < ofs) {
                low = idx + 1;
            } else if (tmp > ofs) {
                high = idx - 1;
            } else {
                return idx;
            }
        }
        throw new RuntimeException("no such ofs: " + ofs);
    }
