    public static final void sequence(int[] list, int above) {
        int temp, max, min;
        boolean tag = true;
        for (int i = list.length - 1; i >= 0; i--) {
            for (int j = 0; j < i; j++) {
                if (above < 0) {
                    if (list[j] < list[j + 1]) {
                        temp = list[j];
                        list[j] = list[j + 1];
                        list[j + 1] = temp;
                        tag = true;
                    }
                } else {
                    if (list[j] > list[j + 1]) {
                        temp = list[j];
                        list[j] = list[j + 1];
                        list[j + 1] = temp;
                        tag = true;
                    }
                }
            }
            if (tag == false) break;
        }
    }
