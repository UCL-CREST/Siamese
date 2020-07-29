    private int _goRecursive(int[] array, int target, int start, int end) {
        if (start > end) return -1;
        int currentPosition = (end + start) / 2;
        if (array[currentPosition] == target) return currentPosition; else if (array[currentPosition] > target) {
            end = currentPosition - 1;
        } else {
            start = currentPosition + 1;
        }
        return _goRecursive(array, target, start, end);
    }
