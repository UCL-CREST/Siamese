    public CovarianceMatrix(short[][] vectors) {
        int vectorLength = vectors[0].length;
        dimension = vectors.length;
        smallMatrix = new double[dimension][dimension];
        for (int rowIndex = 0; rowIndex < smallMatrix.length; rowIndex++) {
            for (int colIndex = 0; colIndex < rowIndex + 1; colIndex++) {
                smallMatrix[rowIndex][colIndex] = 0;
                for (int i = 0; i < vectorLength; i++) {
                    smallMatrix[rowIndex][colIndex] += vectors[rowIndex][i] * vectors[colIndex][i];
                }
                smallMatrix[colIndex][rowIndex] = smallMatrix[rowIndex][colIndex];
            }
        }
        this.tridiagonalize();
        this.performQLalgorithm();
        ArrayList<EigenValueAndVector> tempList = new ArrayList<EigenValueAndVector>(dimension);
        for (int i = 0; i < dimension; i++) {
            double[] eigenVector = new double[vectorLength];
            double length = 0;
            for (int j = 0; j < vectorLength; j++) {
                double value = 0;
                for (int k = 0; k < vectors.length; k++) {
                    value += (vectors[k][j]) * (smallMatrix[k][i]);
                }
                eigenVector[j] = value;
                length += value * value;
            }
            length = Math.sqrt(length);
            for (int j = 0; j < eigenVector.length; j++) {
                eigenVector[j] /= length;
            }
            EigenValueAndVector evv = new EigenValueAndVector();
            evv.eigenVector = eigenVector;
            evv.eigenValue = eig[i];
            tempList.add(evv);
        }
        Collections.sort(tempList);
        eigenValueAndVectors = new EigenValueAndVector[dimension];
        tempList.toArray(eigenValueAndVectors);
    }
