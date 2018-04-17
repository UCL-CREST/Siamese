    public static float[][] TransposeMatrix(float[][] M) {
        int lines = M.length;
        int columns = M[0].length;
        float[][] Mi = new float[columns][lines];
        for (int i = 0; i < lines; i++) {
            for (int j = 0; j < columns; j++) {
                Mi[j][i] = M[i][j];
            }
        }
        return Mi;
    }
