    public int[] sort() {
        boolean t = true;
        int temp = 0;
        int[] mas = new int[N];
        Random rand = new Random();
        for (int i = 0; i < N; i++) {
            mas[i] = rand.nextInt(10) + 1;
        }
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
        return mas;
    }
