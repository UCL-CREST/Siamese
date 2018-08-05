    private TrieNode binarySearch(List<TrieNode> subNodes, char c) {
        int start = 0;
        int end = subNodes.size() - 1;
        int mid = 0;
        while (start <= end) {
            mid = (start + end) / 2;
            TrieNode tn = subNodes.get(mid);
            int cmpValue = tn.getCh() - c;
            if (cmpValue == 0) {
                return tn;
            } else if (cmpValue < 0) {
                start = mid + 1;
            } else if (cmpValue > 0) {
                end = mid - 1;
            }
        }
        return null;
    }
