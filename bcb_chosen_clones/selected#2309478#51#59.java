    public Matrix transpose() {
        Matrix result = new Matrix(columns(), rows());
        for (int i = 0; i < rows(); i++) {
            for (int k = 0; k < columns(); k++) {
                result.elements[k][i] = elements[i][k];
            }
        }
        return result;
    }
