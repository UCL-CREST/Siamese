    private NodeProxy get(int docIdx, NodeId nodeId) {
        if (!isSorted()) sort();
        int low = documentOffsets[docIdx];
        int high = low + (documentLengths[docIdx] - 1);
        int mid, cmp;
        NodeProxy p;
        while (low <= high) {
            mid = (low + high) / 2;
            p = nodes[mid];
            cmp = p.getNodeId().compareTo(nodeId);
            if (cmp == 0) {
                return p;
            }
            if (cmp > 0) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return null;
    }
