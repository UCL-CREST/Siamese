    private int[] sort(int n) {
        int[] mas = new int[n];
        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            mas[i] = rand.nextInt(10) + 1;
        }
        boolean t = true;
        int tmp = 0;
        while (t) {
            t = false;
            for (int i = 0; i < mas.length - 1; i++) {
                if (mas[i] > mas[i + 1]) {
                    tmp = mas[i];
                    mas[i] = mas[i + 1];
                    mas[i + 1] = tmp;
                    t = true;
                }
            }
        }
        return mas;
    }
