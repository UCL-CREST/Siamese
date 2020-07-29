    public float[][] getSubints() {
        int[][] subintsInt = this.getSubints(32, 1, 32);
        float[][] subintsfold = new float[subintsInt[0].length][subintsInt.length];
        for (int i = 0; i < subintsInt.length; i++) {
            for (int j = 0; j < subintsInt[0].length; j++) {
                subintsfold[j][i] = subintsInt[i][j];
            }
        }
        return subintsfold;
    }
