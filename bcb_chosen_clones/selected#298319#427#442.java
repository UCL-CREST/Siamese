    private int binarySearch(double mouseCoord, int[][] pointCoords) {
        int left = 0;
        int right = pointCoords.length;
        while (left < right) {
            int middle = (left + right) / 2;
            if (pointCoords[middle][0] < mouseCoord - size / 2) {
                left = middle + 1;
            } else {
                right = middle;
            }
        }
        if (right < pointCoords.length && isInside(mouseCoord, pointCoords[right][0])) {
            return right;
        }
        return -1;
    }
