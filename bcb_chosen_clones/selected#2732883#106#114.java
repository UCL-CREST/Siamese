    public float[][] transposeMatrix(float[][] mat) {
        float[][] transmat = createNullMatrix(mat[0].length, mat.length);
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[i].length; j++) {
                transmat[j][i] = mat[i][j];
            }
        }
        return transmat;
    }
