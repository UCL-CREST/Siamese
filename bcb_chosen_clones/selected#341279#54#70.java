    public SymmetricMatrix covarianceMatrix() {
        double[][] components = new double[average.length][average.length];
        int j;
        for (int i = 0; i < average.length; i++) {
            for (j = 0; j <= i; j++) {
                components[i][j] = covariance[i][j];
                components[j][i] = components[i][j];
            }
        }
        try {
            return SymmetricMatrix.fromComponents(components);
        } catch (DhbNonSymmetricComponents e) {
            return null;
        } catch (DhbIllegalDimension e) {
            return null;
        }
    }
