    public static int binarySearch(double[] arr, double x) {
        int a = 0, b = arr.length - 1;
        while (a <= b) {
            int i = (a + b) / 2;
            if (arr[i] < x) a = i + 1; else if (arr[i] > x) b = i - 1; else return i;
        }
        return ~a;
    }
