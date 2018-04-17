    public void flipRowCols() {
        if (0 < matrix.length && 0 < matrix[0].length) {
            float[][] newMatrix = new float[matrix[0].length][matrix.length];
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    newMatrix[j][i] = matrix[i][j];
                }
            }
            List<String> backup = colLabels;
            colLabels = rowLabels;
            rowLabels = backup;
            List<Map<String, String>> backList = colLabelsAttrs;
            colLabelsAttrs = rowLabelsAttrs;
            rowLabelsAttrs = backList;
            matrix = newMatrix;
        }
        int backUp = nbRows;
        nbRows = nbCols;
        nbCols = backUp;
        String backup = rowTitle;
        rowTitle = colTitle;
        colTitle = backup;
    }
