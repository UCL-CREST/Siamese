    public EstimatedPolynomial evaluate() {
        for (int i = 0; i < systemConstants.length; i++) {
            for (int j = i + 1; j < systemConstants.length; j++) systemMatrix[i][j] = systemMatrix[j][i];
        }
        try {
            LUPDecomposition lupSystem = new LUPDecomposition(systemMatrix);
            double[][] components = lupSystem.inverseMatrixComponents();
            LUPDecomposition.symmetrizeComponents(components);
            return new EstimatedPolynomial(lupSystem.solve(systemConstants), SymmetricMatrix.fromComponents(components));
        } catch (DhbIllegalDimension e) {
        } catch (DhbNonSymmetricComponents ex) {
        }
        ;
        return null;
    }
