    public void transpose() {
        AffineMatrix transpose = new AffineMatrix();
        for (int i = 0; i < 4; i++) for (int j = 0; j < 4; j++) transpose.m[i][j] = m[j][i];
        m = transpose.m;
    }
