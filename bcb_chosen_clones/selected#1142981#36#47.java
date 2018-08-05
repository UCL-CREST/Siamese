    protected int binarySearchForNearest(int val, int begin, int end) {
        int mid = (begin + end) / 2;
        int midval = array.get(mid);
        if (mid == end) return midval >= val ? mid : -1;
        if (midval < val) {
            if (array.get(mid + 1) >= val) return mid + 1;
            return binarySearchForNearest(val, mid + 1, end);
        } else {
            if (midval == val) return mid;
            return binarySearchForNearest(val, begin, mid);
        }
    }
