    private int[] randomiseCars(int n) {
        int[] cars = new int[n];
        for (int i = 0; i < n; i++) cars[i] = i;
        Random r = new Random();
        for (int i = 0; i < n; i++) {
            int j = i + r.nextInt(n - i);
            int tmp = cars[i];
            cars[i] = cars[j];
            cars[j] = tmp;
        }
        return cars;
    }
