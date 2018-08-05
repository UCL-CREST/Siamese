    public static String[][] performTranspose(String[][] fileData, String[][] fileDataTransposed) {
        CompareUtils.ensureTrue(fileDataTransposed.length == fileData[0].length, "ERROR: Dimensions of output file not correctly set!");
        CompareUtils.ensureTrue(fileDataTransposed[0].length == fileData.length, "ERROR: Dimensions of output file not correctly set!");
        for (int row = 0; row < fileData.length; row++) {
            for (int col = 0; col < fileData[row].length; col++) {
                fileDataTransposed[col][row] = fileData[row][col];
            }
        }
        return fileDataTransposed;
    }
