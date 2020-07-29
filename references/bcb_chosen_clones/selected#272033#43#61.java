    public double[][] getFSDistances() {
        int size = getMatrix().size();
        double[][] distances;
        try {
            distances = new double[size][size];
            for (int i = 0; i < size; i++) {
                double distii = get(i, i);
                for (int j = i; j < size; j++) {
                    distances[i][j] = Math.sqrt(distii + get(j, j) - 2 * get(i, j));
                    distances[j][i] = distances[i][j];
                }
            }
        } catch (OutOfMemoryError e) {
            distances = null;
            System.err.println("Not enough memory for distances!");
            System.gc();
        }
        return distances;
    }
