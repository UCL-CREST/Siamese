    @Override
    public Matrix transpose(Factory factory) {
        if (factory == null) throw new NullPointerException();
        double result[][] = new double[columns][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                result[j][i] = self[i][j];
            }
        }
        return factory.createMatrix(result);
    }
