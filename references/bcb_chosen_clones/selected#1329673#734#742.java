    static int mostSigFibonacci(long[] fibs, long n) {
        int low = 0;
        int high = fibs.length - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            if (fibs[mid] < n) low = (low == mid) ? mid + 1 : mid; else if (fibs[mid] > n) high = (high == mid) ? mid - 1 : mid; else return mid;
        }
        return low - 1;
    }
