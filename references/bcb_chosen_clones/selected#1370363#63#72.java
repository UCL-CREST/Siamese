    private static int binarySearch(char x) {
        int low = 0;
        int high = pinyinVoy.length - 1;
        int mid;
        while (low <= high) {
            mid = (low + high) / 2;
            if (pinyinVoy[mid] < x) low = mid + 1; else if (pinyinVoy[mid] > x) high = mid - 1; else return mid;
        }
        return NOT_FOUND;
    }
