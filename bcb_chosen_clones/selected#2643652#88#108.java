    public int[] getRandMas(int n) {
        boolean t = true;
        int interim = 0;
        int[] mas = new int[n];
        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            mas[i] = rand.nextInt(10) + 1;
        }
        while (t) {
            t = false;
            for (int i = 0; i < mas.length - 1; i++) {
                if (mas[i] > mas[i + 1]) {
                    interim = mas[i];
                    mas[i] = mas[i + 1];
                    mas[i + 1] = interim;
                    t = true;
                }
            }
        }
        return mas;
    }
