    @Override
    public BasicCohoMatrix<V> transpose() {
        V[][] d = createArray(data[0].length, data.length);
        int[][] p = new int[pos[0].length][pos.length];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                d[j][i] = data[i][j];
                p[j][i] = pos[i][j];
            }
        }
        BasicCohoMatrix<V> result = new BasicCohoMatrix(type.zero(), d, p, ncols, nrows, !isDual);
        return result;
    }
