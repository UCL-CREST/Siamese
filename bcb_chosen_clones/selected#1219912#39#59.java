    public void modifyBubble(int[] array) {
        for (int i = 0; i < array.length; i++) {
            if (i % 2 != 0) {
                for (int j = array.length - i / 2 - 2; j >= i / 2; j--) {
                    if (array[j] >= array[j + 1]) {
                        int temp = array[j];
                        array[j] = array[j + 1];
                        array[j + 1] = temp;
                    }
                }
            } else {
                for (int j = i / 2; j < array.length - 1 - i / 2; j++) {
                    if (array[j] >= array[j + 1]) {
                        int temp = array[j];
                        array[j] = array[j + 1];
                        array[j + 1] = temp;
                    }
                }
            }
        }
    }
