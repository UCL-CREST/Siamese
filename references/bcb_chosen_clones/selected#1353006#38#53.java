    public void bubbleSort(final int[] s) {
        source = s;
        if (source.length < 2) return;
        boolean go = true;
        while (go) {
            go = false;
            for (int i = 0; i < source.length - 1; i++) {
                int temp = source[i];
                if (temp > source[i + 1]) {
                    source[i] = source[i + 1];
                    source[i + 1] = temp;
                    go = true;
                }
            }
        }
    }
