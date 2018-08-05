    public int binarySearch(String key, String valueToSearch) {
        IndexRow currRow = myIndex.getRow(key);
        int low = 0;
        int high = currRow.getRowSize() - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            int c = valueToSearch.compareTo(currRow.getIndex(mid).getEntryId());
            if (c < 0) high = mid - 1; else if (c > 0) low = mid + 1; else return mid;
        }
        return -1;
    }
