    public static int[] bubbleSortOtimizado(int... a) {
        boolean swapped;
        int n = a.length - 2;
        do {
            swapped = false;
            for (int i = 0; i <= n; i++) {
                if (a[i] > a[i + 1]) {
                    int tmp = a[i];
                    a[i] = a[i + 1];
                    a[i + 1] = tmp;
                    swapped = true;
                }
            }
            n = n - 1;
        } while (swapped);
        return a;
    }
