    public Matrix44 transpose() {
        Matrix44 result = new Matrix44();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result._m[i][j] = _m[j][i];
            }
        }
        return result;
    }
