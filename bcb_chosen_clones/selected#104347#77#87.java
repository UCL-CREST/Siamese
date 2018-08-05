    protected void computeCoefficients() {
        for (int i = 0; i < systemConstants.length; i++) {
            for (int j = i + 1; j < systemConstants.length; j++) {
                systemMatrix[i][j] = systemMatrix[j][i];
            }
        }
        LUPDecomposition lupSystem = new LUPDecomposition(systemMatrix);
        double[][] components = lupSystem.inverseMatrixComponents();
        LUPDecomposition.symmetrizeComponents(components);
        coefficients = lupSystem.solve(systemConstants);
    }
