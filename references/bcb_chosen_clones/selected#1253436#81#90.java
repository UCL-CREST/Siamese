    protected int binarySearchForNearestAlt(int val, int begin, int end) {
        while (true) {
            int mid = (begin + end) / 2;
            if (mid == end || (baseList.get(mid) <= val && baseList.get(mid + 1) > val)) return mid; else if (baseList.get(mid) < val) {
                begin = mid + 1;
            } else {
                end = mid;
            }
        }
    }
