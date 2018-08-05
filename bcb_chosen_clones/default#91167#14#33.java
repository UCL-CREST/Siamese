    void sort(int a[]) throws Exception {
        for (int i = a.length; --i >= 0; ) {
            boolean flipped = false;
            for (int j = 0; j < i; j++) {
                if (stopRequested) {
                    return;
                }
                if (a[j] > a[j + 1]) {
                    int T = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = T;
                    flipped = true;
                }
                pause(i, j);
            }
            if (!flipped) {
                return;
            }
        }
    }
