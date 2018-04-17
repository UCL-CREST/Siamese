    public int go(int[] array, int target) {
        int start = 0;
        int end = array.length - 1;
        while (end >= start) {
            int currentPosition = (end + start) / 2;
            if (array[currentPosition] == target) return currentPosition; else if (array[currentPosition] > target) {
                end = currentPosition - 1;
            } else {
                start = currentPosition + 1;
            }
        }
        return -1;
    }
