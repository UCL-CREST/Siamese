    public VarNode search(String name) {
        int low = 0;
        int high = variables.size() - 1;
        int mid, cmp;
        VarNode node;
        while (low <= high) {
            mid = (low + high) / 2;
            node = (VarNode) variables.get(mid);
            cmp = node.getIdentifier().compareTo(name);
            if (cmp < 0) low = mid + 1; else if (cmp > 0) high = mid - 1; else return node;
        }
        return null;
    }
