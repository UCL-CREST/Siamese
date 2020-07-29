    private void bubbleSort(int[] mas) {
        boolean t = true;
        int temp = 0;
        while (t) {
            t = false;
            for (int i = 0; i < mas.length - 1; i++) {
                if (mas[i] > mas[i + 1]) {
                    temp = mas[i];
                    mas[i] = mas[i + 1];
                    mas[i + 1] = temp;
                    t = true;
                }
            }
        }
    }
