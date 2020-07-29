    private int binarySearch(int[] table, long target, int lowerBound, int upperBound) {
        int currentIndex;
        currentIndex = (lowerBound + upperBound) / 2;
        if (table[currentIndex] == target || ((table[currentIndex] > target && target > (currentIndex != 0 ? table[currentIndex - 1] : 0)))) {
            return currentIndex;
        } else if (lowerBound > upperBound) {
            return -1;
        } else {
            if (table[currentIndex] < target) {
                return binarySearch(table, target, currentIndex + 1, upperBound);
            } else {
                return binarySearch(table, target, lowerBound, currentIndex - 1);
            }
        }
    }
