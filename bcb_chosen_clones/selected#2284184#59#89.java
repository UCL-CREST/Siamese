    public static void main(String[] args) {
        int size = 10;
        Portfolio port = new Portfolio(size);
        double[] data = new double[size];
        Random rnd = new Random();
        double max = 0, min = 0;
        for (int i = 0; i < size; i++) {
            data[i] = Math.abs(rnd.nextInt() * (1 - rnd.nextDouble()));
            if (max < data[i]) max = data[i];
            if (min > data[i]) min = data[i];
        }
        port.setProfit(data);
        double[][] data2 = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i != j) {
                    data2[i][j] = (rnd.nextBoolean() ? (-1) : 1) * (1 - rnd.nextDouble());
                    data2[j][i] = data2[i][j];
                } else {
                    data2[i][i] = (1 - rnd.nextDouble());
                }
            }
        }
        port.setCovariance(data2);
        double exp = (max + min) / 2;
        try {
            test(port, exp, 0.00000001);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
