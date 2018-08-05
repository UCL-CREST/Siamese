    public static double[][] kinshipRelation(int[][] ped) {
        int n = ped.length;
        int maleParent;
        int femaleParent;
        double[][] aMatrix = new double[n][n];
        for (int i = 0; i < n; i++) {
            aMatrix[i][i] = 1;
            for (int j = i + 1; j < n; j++) {
                aMatrix[i][j] = 0;
                aMatrix[j][i] = 0;
            }
        }
        System.out.println("initial: diagonal 1, 0 otherwise");
        for (int i = 0; i < n; i++) {
            femaleParent = ped[i][1];
            maleParent = ped[i][2];
            if ((femaleParent > 0) && (maleParent > 0)) {
                aMatrix[i][i] = aMatrix[i][i] + .5 * aMatrix[maleParent - 1][femaleParent - 1];
            }
            for (int j = i + 1; j < n; j++) {
                femaleParent = ped[j][1];
                maleParent = ped[j][2];
                if ((femaleParent > 0) && (maleParent > 0)) {
                    aMatrix[i][j] = .5 * (aMatrix[i][femaleParent - 1] + aMatrix[i][maleParent - 1]);
                } else if (maleParent > 0) {
                    aMatrix[i][j] = .5 * aMatrix[i][maleParent - 1];
                } else if (femaleParent > 0) {
                    aMatrix[i][j] = .5 * aMatrix[i][femaleParent - 1];
                } else {
                }
                aMatrix[j][i] = aMatrix[i][j];
            }
        }
        System.out.println("A matrix finished");
        return aMatrix;
    }
