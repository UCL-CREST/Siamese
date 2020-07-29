    public ContingencyTable(double[][] observed) {
        try {
            this.observed = observed;
            this.numberRow = observed.length;
            this.numberCol = observed[0].length;
            this.rowSumObserved = new int[this.numberRow];
            this.colSumObserved = new int[this.numberCol];
            double[][] currentColumnArray = new double[this.numberCol][this.numberRow];
            for (int i = 0; i < this.numberRow; i++) {
                rowSumObserved[i] = (int) AnalysisUtility.sum(observed[i]);
                for (int j = 0; j < this.numberCol; j++) {
                    currentColumnArray[j][i] = observed[i][j];
                    this.grandTotal += observed[i][j];
                }
            }
            for (int j = 0; j < this.numberCol; j++) {
                colSumObserved[j] = (int) AnalysisUtility.sum(currentColumnArray[j]);
            }
        } catch (DataIsEmptyException e) {
        } catch (NullPointerException e) {
        }
    }
