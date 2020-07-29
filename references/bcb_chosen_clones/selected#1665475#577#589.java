    public Matrix transpose() {
        final double arrayRe[][] = new double[numCols][numRows];
        final double arrayIm[][] = new double[numCols][numRows];
        for (int j, i = 0; i < numRows; i++) {
            arrayRe[0][i] = matrixRe[i][0];
            arrayIm[0][i] = matrixIm[i][0];
            for (j = 1; j < numCols; j++) {
                arrayRe[j][i] = matrixRe[i][j];
                arrayIm[j][i] = matrixIm[i][j];
            }
        }
        return new ComplexMatrix(arrayRe, arrayIm);
    }
