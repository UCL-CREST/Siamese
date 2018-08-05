    public InvariantsMatrix transpose() throws Exception {
        int[][] tmpData = new int[width()][height()];
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                tmpData[i][j] = data[j][i];
            }
        }
        return new InvariantsMatrix(tmpData);
    }
