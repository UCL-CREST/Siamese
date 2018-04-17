    public TransformMatrix transpose() {
        double nm[][] = new double[3][3];
        int i, j;
        for (i = 0; i < 3; ++i) {
            for (j = 0; j < 3; ++j) {
                nm[i][j] = m[j][i];
            }
        }
        return new TransformMatrix(nm);
    }
