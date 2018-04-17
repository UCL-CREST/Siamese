    public static JamaMatrix makeCovarianceMatrix(double[][] data_vectors) throws VisADException {
        int dim = data_vectors[0].length;
        int n_vectors = data_vectors.length;
        double[] mean_vector = new double[dim];
        for (int jj = 0; jj < dim; jj++) {
            double sum = 0;
            for (int kk = 0; kk < n_vectors; kk++) {
                sum += data_vectors[kk][jj];
            }
            mean_vector[jj] = sum / n_vectors;
        }
        double[][] cv = new double[dim][dim];
        for (int jj = 0; jj < dim; jj++) {
            for (int ii = jj; ii < dim; ii++) {
                double sum = 0;
                for (int kk = 0; kk < n_vectors; kk++) {
                    sum += (data_vectors[kk][jj] - mean_vector[jj]) * (data_vectors[kk][ii] - mean_vector[ii]);
                }
                cv[jj][ii] = sum / n_vectors;
                cv[ii][jj] = cv[jj][ii];
            }
        }
        return new JamaMatrix(cv);
    }
