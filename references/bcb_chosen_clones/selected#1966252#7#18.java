    public static int search(String[] array, String token, int start, int end) {
        if (start >= end) return 0;
        int midPos = (start + end) / 2;
        int compRes = token.compareTo(array[midPos]);
        if (compRes == 0) {
            return midPos + 1;
        } else if (compRes < 0) {
            return search(array, token, start, midPos);
        } else {
            return search(array, token, midPos + 1, end);
        }
    }
