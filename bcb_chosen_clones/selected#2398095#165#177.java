    public static double[][] reverseDoubleIndex(double[][] input) {
        int iLength = input.length;
        double[][] result = new double[iLength][iLength];
        for (int i = 0; i < iLength; i++) {
            for (int j = 0; j < iLength; j++) {
                try {
                    result[i][j] = input[j][i];
                } catch (Exception e) {
                }
            }
        }
        return result;
    }
