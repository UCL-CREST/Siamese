    public Matrix4 transpose() {
        Matrix4 aux = new Matrix4();
        for (int i = 0; i < 4; i++) for (int j = 0; j < 4; j++) aux.mm[i][j] = mm[j][i];
        assign(aux);
        return this;
    }
