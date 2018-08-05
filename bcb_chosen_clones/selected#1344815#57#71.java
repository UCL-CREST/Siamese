    void sortIds(int a[]) {
        ExecutionTimer t = new ExecutionTimer();
        t.start();
        for (int i = a.length; --i >= 0; ) {
            for (int j = 0; j < i; j++) {
                if (a[j] > a[j + 1]) {
                    int T = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = T;
                }
            }
        }
        t.end();
        TimerRecordFile timerFile = new TimerRecordFile("sort", "BufferSorting", "sortIds", t.duration());
    }
