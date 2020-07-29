    private int getInsertIndex(DefaultMutableTreeNode parent, Object obj) {
        int childCount = parent.getChildCount();
        int index;
        if (_comparator == null) index = childCount; else {
            int low = 0;
            int high = childCount - 1;
            index = low;
            while (low <= high) {
                int mid = (low + high) / 2;
                Object midVal = parent.getChildAt(mid);
                int cmp = _comparator.compare(midVal, obj);
                if (cmp < 0) {
                    low = mid + 1;
                    index = low;
                } else if (cmp > 0) {
                    high = mid - 1;
                    index = low;
                } else {
                    index = mid;
                    break;
                }
            }
        }
        return index;
    }
