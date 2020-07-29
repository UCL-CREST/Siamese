    public static int[] bubbleSort2(int[] source) {
        if (null != source && source.length > 0) {
            boolean flag = false;
            while (!flag) {
                for (int i = 0; i < source.length - 1; i++) {
                    if (source[i] > source[i + 1]) {
                        int temp = source[i];
                        source[i] = source[i + 1];
                        source[i + 1] = temp;
                        break;
                    } else if (i == source.length - 2) {
                        flag = true;
                    }
                }
            }
        }
        return source;
    }
