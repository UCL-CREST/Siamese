    private void bubbleSort(int values[]) {
        PerfMonTimer timerOuter = PerfMonTimer.start("SortingTest.bubbleSort");
        try {
            int len = values.length - 1;
            for (int i = 0; i < len; i++) {
                for (int j = 0; j < len - i; j++) {
                    if (values[j] > values[j + 1]) {
                        int tmp = values[j];
                        values[j] = values[j + 1];
                        values[j + 1] = tmp;
                    }
                }
            }
        } finally {
            PerfMonTimer.stop(timerOuter);
        }
    }
