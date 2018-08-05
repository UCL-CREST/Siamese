    public SingularValueDecomposition computeLeftSigularValueDecompositionMatrix(Matrix xyMatrix) throws IOException {
        Matrix userTagFrequencyMatrix = xyMatrix;
        double[][] dimensionMatrix = userTagFrequencyMatrix.getArray();
        int M = dimensionMatrix.length;
        int N = dimensionMatrix[0].length;
        double[][] cosineSimilairity2DArray = new double[M][M];
        double sm1 = 0;
        double sm2 = 0;
        double sm3 = 0;
        for (int i = 0; i < M; i++) {
            for (int j = i + 1; j < M; j++) {
                sm1 = 0;
                sm2 = 0;
                sm3 = 0;
                for (int k = 0; k < N; k++) {
                    sm1 += dimensionMatrix[i][k] * dimensionMatrix[j][k];
                    sm2 += dimensionMatrix[i][k] * dimensionMatrix[i][k];
                    sm3 += dimensionMatrix[j][k] * dimensionMatrix[j][k];
                }
                cosineSimilairity2DArray[i][j] = sm1 / Math.sqrt(sm2) / Math.sqrt(sm3);
                if (new Double(cosineSimilairity2DArray[i][j]).equals(Double.NaN)) {
                    cosineSimilairity2DArray[i][j] = 0d;
                }
            }
        }
        for (int i = 0; i < M; i++) {
            cosineSimilairity2DArray[i][i] = 1;
            for (int j = i + 1; j < M; j++) {
                cosineSimilairity2DArray[j][i] = cosineSimilairity2DArray[i][j];
            }
        }
        Matrix cosineSimilarityMatrix = new Matrix(cosineSimilairity2DArray);
        int columnDimensionSize = cosineSimilarityMatrix.getColumnDimension();
        double[][] sigmaCosineSimilarity2DArray = new double[columnDimensionSize][columnDimensionSize];
        for (int i = 0; i < columnDimensionSize; i++) {
            for (int j = 0; j < columnDimensionSize; j++) {
                sigmaCosineSimilarity2DArray[i][i] = sigmaCosineSimilarity2DArray[i][i] + cosineSimilairity2DArray[i][j];
            }
        }
        Matrix sigmaCosineSimilarityMatrix = new Matrix(sigmaCosineSimilarity2DArray);
        double squaredSigmaCosineSimilarity2DArray[][] = new double[columnDimensionSize][columnDimensionSize];
        for (int i = 0; i < columnDimensionSize; i++) {
            double dij = sigmaCosineSimilarityMatrix.get(i, i);
            squaredSigmaCosineSimilarity2DArray[i][i] = 1 / Math.sqrt(dij);
            if (new Double(squaredSigmaCosineSimilarity2DArray[i][i]).equals(Double.NaN)) {
                cosineSimilairity2DArray[i][i] = 0d;
            }
        }
        Matrix squaredSigmaCosineSimilarityMatrix = new Matrix(squaredSigmaCosineSimilarity2DArray);
        Matrix laplacianMatrix = calculateLaplacianMatrix(cosineSimilarityMatrix, sigmaCosineSimilarityMatrix, squaredSigmaCosineSimilarityMatrix);
        SingularValueDecomposition sigularValueDecompositionMatrix = laplacianMatrix.svd();
        return sigularValueDecompositionMatrix;
    }
