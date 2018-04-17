    void sort(int a[]) throws Exception {
        int j;
        int limit = a.length;
        int st = -1;
        while (st < limit) {
            boolean flipped = false;
            st++;
            limit--;
            for (j = st; j < limit; j++) {
                if (stopRequested) {
                    return;
                }
                if (a[j] > a[j + 1]) {
                    int T = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = T;
                    flipped = true;
                    pause(st, limit);
                }
            }
            if (!flipped) {
                return;
            }
            for (j = limit; --j >= st; ) {
                if (stopRequested) {
                    return;
                }
                if (a[j] > a[j + 1]) {
                    int T = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = T;
                    flipped = true;
                    pause(st, limit);
                }
            }
            if (!flipped) {
                return;
            }
        }
        pause(st, limit);
    }
