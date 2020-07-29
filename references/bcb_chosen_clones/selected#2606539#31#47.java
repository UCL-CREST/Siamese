    public static int[] bubbleSort(int[] source) {
        if (source != null && source.length > 0) {
            boolean flag = true;
            while (flag) {
                flag = false;
                for (int i = 0; i < source.length - 1; i++) {
                    if (source[i] > source[i + 1]) {
                        int temp = source[i];
                        source[i] = source[i + 1];
                        source[i + 1] = temp;
                        flag = true;
                    }
                }
            }
        }
        return source;
    }
