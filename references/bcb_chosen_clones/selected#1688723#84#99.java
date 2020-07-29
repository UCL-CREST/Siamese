    public void setExpected(double[][] expected) {
        double[][] currentColumnArray = new double[this.numberCol][this.numberRow];
        try {
            for (int i = 0; i < this.numberRow; i++) {
                rowSumExpected[i] = (int) AnalysisUtility.sum(expected[i]);
                for (int j = 0; j < this.numberCol; j++) {
                    currentColumnArray[j][i] = expected[i][j];
                }
            }
            for (int j = 0; j < this.numberCol; j++) {
                colSumExpected[j] = (int) AnalysisUtility.sum(currentColumnArray[j]);
            }
        } catch (DataIsEmptyException e) {
        } catch (NullPointerException e) {
        }
    }
