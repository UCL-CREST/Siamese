    public void calculateDistances() {
        distances = new double[numPoints][numPoints];
        double[] Y_i, Y_j;
        for (int i = 0; i < numPoints; i++) {
            Y_i = (double[]) getPoint(i);
            for (int j = i + 1; j < numPoints; j++) {
                Y_j = (double[]) getPoint(j);
                distances[i][j] = 0.00;
                for (int k = 0; k < dimensions; ++k) {
                    distances[i][j] += (Y_i[k] - Y_j[k]) * (Y_i[k] - Y_j[k]);
                }
                distances[i][j] = Math.sqrt(distances[i][j]);
                distances[j][i] = distances[i][j];
            }
        }
    }
