    private int binarySearchGet(int down, int up, Constant searchkey) {
        if (getData(getRecordNum() - 1).compareTo(searchkey) < 0) {
            return getRecordNum() - 1;
        }
        int mid = (down + up) / 2;
        int ans = getData(mid).compareTo(searchkey);
        if (ans == 0) return mid;
        if (ans > 0) {
            if (getData(up).compareTo(searchkey) <= 0) return mid; else return binarySearchGet(down, mid - 1, searchkey);
        } else {
            if (getData(down).compareTo(searchkey) >= 0) return mid; else return binarySearchGet(mid + 1, up, searchkey);
        }
    }
