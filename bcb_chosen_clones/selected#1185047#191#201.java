    private int _getResourceIndex(String ID) {
        if (resources.size() == 0) return -1;
        int start = 0;
        int end = resources.size() - 1;
        while (start <= end) {
            int mid = (end + start) / 2;
            int comp = ((String) resources.elementAt(mid, 1)).compareToIgnoreCase(ID);
            if (comp == 0) return mid; else if (comp > 0) end = mid - 1; else start = mid + 1;
        }
        return -1;
    }
