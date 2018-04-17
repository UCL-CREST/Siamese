    public ImportNode search(String name) {
        int low = 0;
        int high = imports.size() - 1;
        int mid, cmp;
        ImportNode node;
        while (low <= high) {
            mid = (low + high) / 2;
            node = (ImportNode) imports.get(mid);
            cmp = node.name.compareTo(name);
            if (cmp < 0) low = mid + 1; else if (cmp > 0) high = mid - 1; else return node;
        }
        return null;
    }
