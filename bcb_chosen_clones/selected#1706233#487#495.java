    private float[][] transpose(float[][] m) {
        float[][] toReturn = new float[m[0].length][m.length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                toReturn[j][i] = m[i][j];
            }
        }
        return toReturn;
    }
