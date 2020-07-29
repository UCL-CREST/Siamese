        public void setPosition(NodeProxy proxy) {
            int docIdx = findDoc(proxy.getDocument());
            if (docIdx > -1) {
                int low = documentOffsets[docIdx];
                int high = low + (documentLengths[docIdx] - 1);
                int mid, cmp;
                NodeProxy p;
                while (low <= high) {
                    mid = (low + high) / 2;
                    p = nodes[mid];
                    cmp = p.getNodeId().compareTo(proxy.getNodeId());
                    if (cmp == 0) {
                        pos = mid;
                        return;
                    }
                    if (cmp > 0) {
                        high = mid - 1;
                    } else {
                        low = mid + 1;
                    }
                }
            }
            pos = -1;
        }
