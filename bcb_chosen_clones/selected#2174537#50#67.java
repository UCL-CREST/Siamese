    public static int binarySearch(final WordList wordList, final String word) {
        final Comparator<String> comparator = wordList.getComparator();
        int low = 0;
        int high = wordList.size() - 1;
        int mid;
        while (low <= high) {
            mid = (low + high) / 2;
            final int cmp = comparator.compare(wordList.get(mid), word);
            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return NOT_FOUND;
    }
