    private void symmetrizeMatrix() {
        for (int i = 0; i < systemConstants.length; i++) {
            for (int j = 0; j < i; j++) systemMatrix[j][i] = systemMatrix[i][j];
        }
    }
