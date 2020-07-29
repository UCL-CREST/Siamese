    public void transpose() {
        int[][] tempContent;
        int matrixLength = matrixContent.length;
        tempContent = new int[matrixLength][];
        for (int i = 0; i < matrixLength; i++) {
            tempContent[i] = new int[matrixLength];
            Arrays.fill(tempContent[i], 0);
        }
        for (int i = 0; i < matrixLength; i++) for (int j = 0; j < matrixLength; j++) tempContent[i][j] = matrixContent[j][i];
        matrixContent = tempContent.clone();
    }
