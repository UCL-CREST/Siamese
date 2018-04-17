        public void setPosition(NodeProxy node) {
            next = null;
            docIdx = findDoc(node.getDocument());
            if (docIdx > -1) {
                int low = documentOffsets[docIdx];
                int high = low + (documentLengths[docIdx] - 1);
                int mid, cmp;
                NodeProxy p;
                while (low <= high) {
                    mid = (low + high) / 2;
                    p = nodes[mid];
                    cmp = p.getNodeId().compareTo(node.getNodeId());
                    if (cmp == 0) {
                        pos = mid - documentOffsets[docIdx];
                        return;
                    }
                    if (cmp > 0) {
                        high = mid - 1;
                    } else {
                        low = mid + 1;
                    }
                }
            }
        }
