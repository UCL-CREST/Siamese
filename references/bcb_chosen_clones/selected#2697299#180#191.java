    private int getIndexForRangeSplit(long[] splitIndices, long rootIndex, int min, int max) {
        if (rootIndex == splitIndices[min] || rootIndex == splitIndices[max]) {
            System.out.println("Error: root index (" + rootIndex + ") shouldn't equal splitIndices[min] (" + splitIndices[min] + ") or splitIndices[max] (" + splitIndices[max] + ") when finding a split range");
        }
        int mid = (min + max) / 2;
        while (min + 1 < max && splitIndices[mid] != rootIndex) {
            if (splitIndices[mid] < rootIndex) min = mid; else max = mid;
            mid = (min + max) / 2;
        }
        mid = (min + max) / 2;
        return mid;
    }
