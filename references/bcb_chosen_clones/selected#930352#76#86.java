    public boolean InitializeDistances() {
        Distances = new double[nrow][nrow];
        for (int i = 0; i < nrow; i++) {
            for (int j = 0; j < i; j++) {
                Distances[i][j] = DPM.Dist(Net[i], Net[j], ncol);
                Distances[j][i] = Distances[i][j];
            }
            Distances[i][i] = DPM.Dist(Net[i], Net[i], ncol);
        }
        return true;
    }
