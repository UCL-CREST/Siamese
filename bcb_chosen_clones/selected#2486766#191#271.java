    private void fillProductResult() {
        this.ProductResult = new int[this.linearDim + 1][this.linearDim + 1];
        for (int j = 1; j < this.linearDim + 1; j++) {
            this.ProductResult[1][j] = j;
            this.ProductResult[j][1] = j;
        }
        int[] doubleSort = new int[2 * this.GradeCount - 1];
        int permuteCounter = 0;
        int doubleKey = 0;
        int j = 2;
        int k = 2;
        int m = 1;
        int n = 1;
        int tempSort = 0;
        for (j = 2; j < this.linearDim + 1; j++) {
            for (k = 2; k < this.linearDim + 1; k++) {
                permuteCounter = 0;
                doubleKey = 0;
                for (m = 1; m < this.GradeCount; m++) {
                    doubleSort[m] = this.ABasis.getBasis(j, m);
                    doubleSort[m + this.GradeCount - 1] = this.ABasis.getBasis(k, m);
                }
                m = 1;
                for (m = 1; m < 2 * this.GradeCount - 1; m++) {
                    for (n = 1; n < 2 * this.GradeCount - 2; n++) {
                        if (doubleSort[n] > doubleSort[n + 1]) {
                            tempSort = doubleSort[n];
                            doubleSort[n] = doubleSort[n + 1];
                            doubleSort[n + 1] = tempSort;
                            if (!(doubleSort[n] == 0 || doubleSort[n + 1] == 0)) {
                                permuteCounter += 1;
                            }
                        }
                    }
                    n = 1;
                }
                m = 1;
                permuteCounter = permuteCounter % 2;
                for (m = 1; m < 2 * this.GradeCount - 2; m++) {
                    if (doubleSort[m] == 0) continue;
                    if (doubleSort[m] == doubleSort[m + 1]) {
                        tempSort = doubleSort[m];
                        doubleSort[m] = 0;
                        doubleSort[m + 1] = 0;
                        m += 1;
                        permuteCounter += intSignature[tempSort - 1];
                    }
                }
                m = 1;
                permuteCounter = permuteCounter % 2;
                for (m = 1; m < 2 * this.GradeCount - 1; m++) {
                    for (n = 1; n < 2 * this.GradeCount - 2; n++) {
                        if (doubleSort[n] > doubleSort[n + 1]) {
                            tempSort = doubleSort[n];
                            doubleSort[n] = doubleSort[n + 1];
                            doubleSort[n + 1] = tempSort;
                            if (!(doubleSort[n] == 0 || doubleSort[n + 1] == 0)) {
                                permuteCounter += 1;
                            }
                        }
                    }
                    n = 1;
                }
                m = 1;
                permuteCounter = permuteCounter % 2;
                for (m = 1; m < 2 * this.GradeCount - 1; m++) {
                    doubleKey += (int) doubleSort[m] * Math.pow(this.GradeCount, 2 * this.GradeCount - 2 - m);
                }
                m = 1;
                this.ProductResult[j][k] = 0;
                for (m = 1; m < this.linearDim + 1; m++) {
                    if (doubleKey == this.ABasis.getBasisKey(m)) {
                        this.ProductResult[j][k] = m * (int) Math.pow(-1.0, permuteCounter);
                        break;
                    }
                }
                m = 1;
            }
            k = 2;
        }
    }
