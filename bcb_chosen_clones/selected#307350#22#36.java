    @Override
    public void sortArray(int[] array) {
        boolean sorted = false;
        while (sorted == false) {
            sorted = true;
            for (int i = 0; i <= array.length - 2; i++) {
                if (array[i] > array[i + 1]) {
                    int temp = array[i];
                    array[i] = array[i + 1];
                    array[i + 1] = temp;
                    sorted = false;
                }
            }
        }
    }
