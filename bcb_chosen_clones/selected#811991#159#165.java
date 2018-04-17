    private boolean binarySearch(int down, int up, Constant searchkey) {
        if (down > up) return false;
        int mid = (down + up) / 2;
        int ans = getData(mid).compareTo(searchkey);
        if (ans == 0) return true;
        if (ans > 0) return binarySearch(down, mid - 1, searchkey); else return binarySearch(mid + 1, up, searchkey);
    }
