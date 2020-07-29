    public void getTranspose(Matrix4 mat) {
        for (int i = 0; i < 4; i++) for (int j = 0; j < 4; j++) mat.mm[i][j] = mm[j][i];
    }
