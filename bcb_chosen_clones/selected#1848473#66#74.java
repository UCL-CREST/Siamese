    public RoutesMap getInverse() {
        DenseRoutesMap transposed = new DenseRoutesMap(distances.length);
        for (int i = 0; i < distances.length; i++) {
            for (int j = 0; j < distances.length; j++) {
                transposed.distances[i][j] = distances[j][i];
            }
        }
        return transposed;
    }
