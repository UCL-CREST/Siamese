    protected int binarySearchForNearest(int val, int begin, int end) {
        int mid = (begin + end) / 2;
        if (mid == end || (baseList.get(mid) <= val && baseList.get(mid + 1) > val)) return mid; else if (baseList.get(mid) < val) return binarySearchForNearest(val, mid + 1, end); else return binarySearchForNearest(val, begin, mid);
    }
