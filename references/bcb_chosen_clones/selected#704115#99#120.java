    public static int orderedFind(Vector orderedArray, IComparable obj) {
        int size = orderedArray.size();
        if (size == 0) {
            return -1;
        }
        int begin = 0;
        int end = size - 1;
        int mid, cmp;
        while (begin <= end) {
            mid = (end + begin) / 2;
            IComparable obj2 = (IComparable) orderedArray.elementAt(mid);
            cmp = obj.compares(obj2);
            if (cmp == 0) {
                return mid;
            } else if (cmp < 0) {
                end = mid - 1;
            } else {
                begin = mid + 1;
            }
        }
        return -1;
    }
