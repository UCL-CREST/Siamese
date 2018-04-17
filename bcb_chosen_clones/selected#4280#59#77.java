    public static void sortSeries(double[] series) {
        if (series == null) {
            throw new IllegalArgumentException("Incorrect series. It's null-pointed");
        }
        int k = 0;
        int right = series.length - 1;
        while (right > 0) {
            k = 0;
            for (int i = 0; i <= right - 1; i++) {
                if (series[i] > series[i + 1]) {
                    k = i;
                    double tmp = series[i];
                    series[i] = series[i + 1];
                    series[i + 1] = tmp;
                }
            }
            right = k;
        }
    }
