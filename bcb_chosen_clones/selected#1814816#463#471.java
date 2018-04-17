    public static double[][] transpose(final double[][] m) {
        final double[][] mt = new double[m[0].length][m.length];
        for (int i = 0; i < mt.length; i++) {
            for (int j = 0; j < mt[i].length; j++) {
                mt[i][j] = m[j][i];
            }
        }
        return mt;
    }
