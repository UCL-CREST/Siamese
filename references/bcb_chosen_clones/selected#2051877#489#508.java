    private int search(BTreeNode cluster, _IndexPredicate[] condition, _VariableValues reader, int low, int high, boolean flag) throws DException {
        if (condition == null) return flag ? low : high;
        int cmp = 0;
        int position = -1;
        int length = 0;
        for (int i = 0; i < condition.length && condition[i] != null; i++, length++) ;
        Object object = null;
        while (low <= high) {
            int mid = (low + high) / 2;
            object = cluster.getKey(mid);
            ((BTreeReader) reader).setValue(object);
            cmp = evaluate1(condition, reader, length);
            if (cmp > 0) low = mid + 1; else if (cmp < 0) high = mid - 1; else {
                cmp = evaluate(condition, reader, length);
                if (cmp == 0) position = mid;
                if (flag) high = mid - 1; else low = mid + 1;
            }
        }
        return position == -1 ? -(low - 1) : position;
    }
