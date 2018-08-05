    public RealSquareMatrix copyUpperToLower() {
        for (int i = 0; i < cols - 1; i++) {
            for (int j = i + 1; j < cols; j++) {
                flmat[j][i] = flmat[i][j];
            }
        }
        return this;
    }
