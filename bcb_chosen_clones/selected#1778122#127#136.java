    public static boolean[][] transpose(boolean[][] booleanBox) {
        YeriDebug.ASSERT(CollectionsToolkit.isRectangular(booleanBox));
        boolean[][] returnBox = new boolean[booleanBox[0].length][booleanBox.length];
        for (int i = 0; i < booleanBox.length; i++) {
            for (int j = 0; j < booleanBox[i].length; j++) {
                returnBox[j][i] = booleanBox[i][j];
            }
        }
        return returnBox;
    }
