    public void transpose() {
        for (int i = 0; i < ndims; i++) {
            for (int j = i + 1; j < ndims; j++) {
                g[j][i] = g[i][j];
            }
        }
    }
