    private static int binarySearch(double[] a, double key, int low, int high) {
        while (low <= high) {
            int mid = (low + high) / 2;
            double midVal = a[mid];
            int cmp;
            if (midVal < key) {
                cmp = -1;
            } else if (midVal > key) {
                cmp = 1;
            } else {
                long midBits = Double.doubleToLongBits(midVal);
                long keyBits = Double.doubleToLongBits(key);
                cmp = (midBits == keyBits ? 0 : (midBits < keyBits ? -1 : 1));
            }
            if (cmp < 0) low = mid + 1; else if (cmp > 0) high = mid - 1; else return mid;
        }
        return -(low + 1);
    }
