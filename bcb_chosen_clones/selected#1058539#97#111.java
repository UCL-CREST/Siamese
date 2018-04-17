    private int binarySearch(int[] A, int N) {
        int lowestPossibleLoc = 0;
        int highestPossibleLoc = A.length - 1;
        while (highestPossibleLoc >= lowestPossibleLoc) {
            int middle = (lowestPossibleLoc + highestPossibleLoc) / 2;
            if (A[middle] == N) {
                return middle;
            } else if (A[middle] > N) {
                highestPossibleLoc = middle - 1;
            } else {
                lowestPossibleLoc = middle + 1;
            }
        }
        return -1;
    }
