    protected int getGlobalIndex(Vector list, String name) {
        if (list.size() == 0) return -1;
        int start = 0;
        int end = list.size() - 1;
        while (start <= end) {
            int mid = (end + start) / 2;
            int comp = ((Environmental) list.elementAt(mid)).Name().compareToIgnoreCase(name);
            if (comp == 0) return mid; else if (comp > 0) end = mid - 1; else start = mid + 1;
        }
        return -1;
    }
