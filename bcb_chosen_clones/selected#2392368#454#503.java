    public static double[][] kinshipRelation(int[][] completedTable) {
        int length = completedTable.length;
        double[][] relationMatrix = new double[length + 1][length + 1];
        for (int i = 0; i < relationMatrix.length; i++) {
            for (int j = 0; j < relationMatrix.length; j++) {
                if (i == 0) {
                    relationMatrix[i][j] = j;
                } else if (j == 0) {
                    relationMatrix[i][j] = i;
                } else if (i == j) {
                    relationMatrix[i][j] = 1;
                } else {
                    relationMatrix[i][j] = 0;
                }
            }
        }
        int maleParent = 0;
        int femaleParent = 0;
        for (int i = 1; i < relationMatrix.length; i++) {
            for (int j = 1; j < relationMatrix.length; j++) {
                if (j > i) {
                    femaleParent = completedTable[j - 1][1];
                    maleParent = completedTable[j - 1][2];
                    if ((femaleParent == 0) && (maleParent == 0)) {
                        relationMatrix[i][j] = 0;
                    } else if (femaleParent == 0) {
                        relationMatrix[i][j] = .5 * relationMatrix[i][maleParent];
                    } else if (maleParent == 0) {
                        relationMatrix[i][j] = .5 * relationMatrix[i][femaleParent];
                    } else {
                        relationMatrix[i][j] = .5 * (relationMatrix[i][femaleParent] + relationMatrix[i][maleParent]);
                    }
                }
            }
        }
        for (int i = 1; i < relationMatrix.length; i++) {
            for (int j = 1; j < relationMatrix.length; j++) {
                if (i > j) {
                    relationMatrix[i][j] = relationMatrix[j][i];
                }
            }
        }
        double tmp[][] = new double[relationMatrix.length - 1][relationMatrix.length - 1];
        for (int i = 0; i < tmp.length; i++) {
            for (int j = 0; j < tmp.length; j++) {
                tmp[i][j] = relationMatrix[i + 1][j + 1];
            }
        }
        return relationMatrix;
    }
