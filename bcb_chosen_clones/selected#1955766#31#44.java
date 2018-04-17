    public static void main(String[] args) throws TSException {
        for (int i = 2; i <= a.getMaxSize(); i++) {
            double[][] distanceMatrix = new double[i][i];
            double[] cuts = a.getCuts(i);
            for (int j = 0; j < i; j++) {
                for (int k = j + 2; k < i; k++) {
                    distanceMatrix[j][k] = Math.sqrt((cuts[j] - cuts[k - 1]) * (cuts[j] - cuts[k - 1]));
                    distanceMatrix[k][j] = distanceMatrix[j][k];
                }
            }
            String s = "Size " + i + ": \n" + MatrixFactory.toString(distanceMatrix);
            System.out.println(s);
        }
    }
