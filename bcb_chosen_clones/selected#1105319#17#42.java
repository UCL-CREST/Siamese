    public String elementsSearch() {
        int index = 0;
        for (int i1 = 0; i1 < 6; i1++) {
            for (int i2 = 0; i2 < 5; i2++) {
                if (index < 5) {
                    if (initialMatrix[i1][i2] > 0) {
                        finalMatrix[index] = initialMatrix[i1][i2];
                        index++;
                    }
                } else break;
            }
        }
        int temp;
        for (int i = 0; i < finalMatrix.length; i++) {
            for (int j = 0; j < finalMatrix.length - 1; j++) {
                if (finalMatrix[j] < finalMatrix[j + 1]) {
                    temp = finalMatrix[j];
                    finalMatrix[j] = finalMatrix[j + 1];
                    finalMatrix[j + 1] = temp;
                }
            }
        }
        String result = "";
        for (int k : finalMatrix) result += k + " ";
        return result;
    }
