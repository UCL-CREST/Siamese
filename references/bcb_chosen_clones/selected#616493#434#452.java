    private String aliasLookup(String alias) {
        int aliasListOffset = getAliasListOffset();
        int min = 0;
        int max = content.getInt(aliasListOffset) - 1;
        while (max >= min) {
            int mid = (min + max) / 2;
            int aliasOffset = content.getInt((aliasListOffset + 4) + (mid * 8));
            int mimeOffset = content.getInt((aliasListOffset + 4) + (mid * 8) + 4);
            int cmp = getMimeType(aliasOffset).compareTo(alias);
            if (cmp < 0) {
                min = mid + 1;
            } else if (cmp > 0) {
                max = mid - 1;
            } else {
                return getMimeType(mimeOffset);
            }
        }
        return null;
    }
