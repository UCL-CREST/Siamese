    public static void orderedInsert(Vector orderedArray, IComparable obj) {
        int size = orderedArray.size();
        if (size == 0) {
            orderedArray.addElement(obj);
            return;
        }
        int begin = 0;
        int end = size - 1;
        int mid = 0, cmp = 0;
        while (begin <= end) {
            mid = (end + begin) / 2;
            IComparable obj2 = (IComparable) orderedArray.elementAt(mid);
            cmp = obj.compares(obj2);
            if (cmp == 0) {
                orderedArray.insertElementAt(obj, mid);
                return;
            } else if (cmp < 0) {
                end = mid - 1;
            } else {
                begin = mid + 1;
            }
        }
        if (cmp < 0) {
            orderedArray.insertElementAt(obj, mid);
        } else {
            orderedArray.insertElementAt(obj, mid + 1);
        }
    }
