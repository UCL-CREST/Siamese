    public static int[] rank(double[] data) {
        int[] rank = new int[data.length];
        for (int i = 0; i < data.length; i++) rank[i] = i;
        boolean swapped;
        double dtmp;
        int i, j, itmp;
        for (i = 0; i < data.length - 1; i++) {
            swapped = false;
            for (j = 0; j < data.length - 1 - i; j++) {
                if (data[j] < data[j + 1]) {
                    dtmp = data[j];
                    data[j] = data[j + 1];
                    data[j + 1] = dtmp;
                    itmp = rank[j];
                    rank[j] = rank[j + 1];
                    rank[j + 1] = itmp;
                    swapped = true;
                }
            }
        }
        return rank;
    }
