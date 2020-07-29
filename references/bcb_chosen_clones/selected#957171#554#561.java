    public RealSquareMatrix copyLowerToUpper() {
        for (int i = 0; i < cols - 1; i++) {
            for (int j = i + 1; j < cols; j++) {
                flmat[i][j] = flmat[j][i];
            }
        }
        return this;
    }
