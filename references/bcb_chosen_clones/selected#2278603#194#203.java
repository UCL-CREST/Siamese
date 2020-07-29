    public double[][] getMatrixFromTree(double p) {
        double[][] Mat = new double[numdata][numdata];
        for (int i = 0; i < numdata; i++) {
            for (int j = 0; j < i; j++) {
                Mat[i][j] = -p * Math.log(getTreeDist(i, j));
                Mat[j][i] = Mat[i][j];
            }
        }
        return Mat;
    }
