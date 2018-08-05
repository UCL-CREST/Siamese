    public static void transpose(float[][] m, float[][] result) {
        for (int i = 0; i < 4; i++) for (int j = 0; j < 4; j++) result[j][i] = m[i][j];
    }
