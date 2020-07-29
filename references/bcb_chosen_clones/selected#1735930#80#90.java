    private static final int search(NodeProxy[] items, int low, int high, DocumentImpl cmpDoc, long gid) {
        int mid;
        int cmp;
        while (low <= high) {
            mid = (low + high) / 2;
            if (items[mid].getDocument().docId == cmpDoc.docId) {
                if (items[mid].gid == gid) return mid; else if (items[mid].gid > gid) high = mid - 1; else low = mid + 1;
            } else if (items[mid].getDocument().docId > cmpDoc.docId) high = mid - 1; else low = mid + 1;
        }
        return -1;
    }
