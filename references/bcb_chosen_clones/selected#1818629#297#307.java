    public float[][] transposeMatrix(float[][] a) {
        int n = a.length;
        int m = a[0].length;
        float[][] b = new float[m][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                b[j][i] = a[i][j];
            }
        }
        return (b);
    }
