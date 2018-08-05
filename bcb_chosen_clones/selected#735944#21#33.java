    private static int binarySearch(int[] array, int leftIndex, int rightIndex, int value) {
        if (leftIndex == rightIndex && array[leftIndex] != value) {
            return -1;
        }
        int middleIndex = (leftIndex + rightIndex) / 2;
        if (array[middleIndex] == value) {
            return middleIndex;
        } else if (array[middleIndex] > value) {
            return binarySearch(array, middleIndex + 1, rightIndex, value);
        } else {
            return binarySearch(array, leftIndex, middleIndex - 1, value);
        }
    }
