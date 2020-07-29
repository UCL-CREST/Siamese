    private static int binarySearch(int query, int[] marbles, int i, int j) {
        if (i > j) {
            return -1;
        }
        int medium = (i + j) / 2;
        if (marbles[medium] == query) {
            return medium;
        }
        if (query < marbles[medium]) {
            return binarySearch(query, marbles, i, medium - 1);
        } else {
            return binarySearch(query, marbles, medium + 1, j);
        }
    }
