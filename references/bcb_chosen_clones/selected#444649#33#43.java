    void composeMatrix() {
        double[] values = decomposition.getParameterValues();
        for (int i = 0; i < dim; i++) {
            for (int j = i; j < dim; j++) {
                matrix[i][j] = 0.0;
                for (int k = 0; k <= i; k++) matrix[i][j] += values[index[i] + k] * values[index[j] + k];
                matrix[j][i] = matrix[i][j];
            }
        }
        compositionKnown = true;
    }
