    public static double[][] transpose(double[][] doubleBox) {
        YeriDebug.ASSERT(CollectionsToolkit.isRectangular(doubleBox));
        double[][] returnBox = new double[doubleBox[0].length][doubleBox.length];
        for (int i = 0; i < doubleBox.length; i++) {
            for (int j = 0; j < doubleBox[i].length; j++) {
                returnBox[j][i] = doubleBox[i][j];
            }
        }
        return returnBox;
    }
