    public FetchGroup getFetchGroup(String gname) {
        if (gname.equals(FetchGroup.DFG_NAME)) return fetchGroups[0];
        int low = 1;
        int high = sortedFetchGroups == null ? 0 : sortedFetchGroups.length - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            FetchGroup midVal = sortedFetchGroups[mid];
            int cmp = midVal.name.compareTo(gname);
            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return midVal;
            }
        }
        return null;
    }
